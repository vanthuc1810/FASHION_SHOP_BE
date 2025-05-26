package com.example.FashionShop.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.FashionShop.Dto.request.QRBank.PaymentLinkRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.CardItem;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.SalesOrder;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.SalesOrderStatus;
import com.example.FashionShop.IServices.IQRBankService;
import com.example.FashionShop.Repository.ProductRepository;
import com.example.FashionShop.Repository.SalesOrderRepository;
import com.example.FashionShop.Repository.UserRepository;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.payos.PayOS;
import vn.payos.type.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QRBankService implements IQRBankService {
    SalesOrderRepository salesOrderRepository;
    @NonFinal
    @Value("${urlServer}")
    private String urlServer;
    @NonFinal
    @Value("${clientId}")
    private String clientId;
    @NonFinal
    @Value("${apiKey}")
    private String apiKey;
    @NonFinal
    @Value("${checkSumKey}")
    private String checkSumKey;
    @NonFinal
    @Value("${urlFE}")
    private String urlFE;

    UserRepository userRepository;
    ProductRepository productRepository;
    @Override
    public CheckoutResponseData createPaymentLink(PaymentLinkRequest paymentLinkRequest) throws Exception {

        PayOS payOS = new PayOS(clientId, apiKey, checkSumKey);

        SalesOrder salesOrder = salesOrderRepository
                .findById(paymentLinkRequest.getIdSalesOrder())
                .orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));

        // check so luong san pham
        for(CardItem cardItem : salesOrder.getCard().getCardItems()){
            Product product = cardItem.getProduct();
            int quantity = cardItem.getQuantity();
            int quantityProduct = product.getQuantity();

            if(quantityProduct < quantity){
                throw new AppException(ErrorCode.QUANTITY_INVALID);
            }
        }


        double totalPrice = salesOrder.getCard().getTotalPrice();

        // Tao idOrder ngau nhien
        long timeStamp = System.currentTimeMillis();
        String idCreate = timeStamp + salesOrder.getIdSalesOrder().toString();
        Long idOrder = Long.valueOf(idCreate);
        PaymentData paymentData = PaymentData.builder()
                .orderCode(idOrder)
                .amount((int) totalPrice)
                .description("Thanh toan don hang")
                .returnUrl(urlFE)
                .cancelUrl(urlFE)
                .build();
        CheckoutResponseData checkoutResponseData = payOS.createPaymentLink(paymentData);
        payOS.confirmWebhook(urlServer+"/recieveWebhook");
        return checkoutResponseData;
    }
    @Override
    public PaymentLinkData checkPaymentLink(Long idOrder) throws Exception {
        PayOS payOS = new PayOS(clientId, apiKey, checkSumKey);
        PaymentLinkData paymentLinkData = payOS.getPaymentLinkInformation(idOrder);

        return paymentLinkData;
    }
    @Override
    public ApiResponse recieveWebhook(Webhook data) {
        try {
            int idOrder = Integer.valueOf(data.getData().getOrderCode().toString().substring(13));
            SalesOrder salesOrder = salesOrderRepository.findById(idOrder).orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));

            if (salesOrder.getStatus().equals(SalesOrderStatus.PENDING_PAYMENT.name()))
            {
                salesOrder.setStatus(SalesOrderStatus.IN_PROGRESS.name());
                salesOrder.setTimeFinished(LocalDateTime.now());
                List<CardItem> cardItemList = salesOrder.getCard().getCardItems();
                List<Product> productList = new ArrayList<>();
                for(CardItem cardItem : cardItemList){
                    Product product = cardItem.getProduct();
                    int quantity = cardItem.getQuantity();
                    int quantityProduct = product.getQuantity();

                    if(quantityProduct < quantity){
                        User user = salesOrder.getUser();
                        float refund = salesOrder.getCard().getTotalPrice();
                        user.setWallet(user.getWallet() + refund);
                        userRepository.save(user);
                        salesOrder.setStatus(SalesOrderStatus.PENDING_PAYMENT.name());
                        salesOrderRepository.save(salesOrder);
                        throw new AppException(ErrorCode.QUANTITY_INVALID);
                    }
                    product.setQuantity(quantityProduct - quantity);
                    productList.add(product);
                }
                productRepository.saveAll(productList);
            }
            salesOrderRepository.save(salesOrder);
        }catch (Exception e) {

        }
        return ApiResponse.builder()
                .results(data)
                .build();
    }
}
