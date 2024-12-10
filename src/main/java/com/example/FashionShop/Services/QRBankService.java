package com.example.FashionShop.Services;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import com.example.FashionShop.Dto.request.QRBank.PaymentLinkRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.Card;
import com.example.FashionShop.Entity.SalesOrder;
import com.example.FashionShop.Enum.SalesOrderStatus;
import com.example.FashionShop.Repository.CardRepository;
import com.example.FashionShop.Repository.SalesOrderRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.FashionShop.Dto.request.QRBank.CartItemRequest;
import com.example.FashionShop.Dto.request.QRBank.CartRequest;
import com.example.FashionShop.Dto.request.QRBank.QRBankRequest;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.payos.PayOS;
import vn.payos.type.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QRBankService {

    SalesOrderRepository salesOrderRepository;
    CardRepository cardRepository;

    private static final Logger log = LoggerFactory.getLogger(QRBankService.class);
    RestTemplate restTemplate;
    ProductRepository productRepository;

    String url = "https://api.vietqr.io/v2/generate";

    String accountNo = "0976049347";

    String accountName = "PHAN VAN THUC";

    String acqId = "970422";
    private static final SecureRandom secureRandom = new SecureRandom();

    @NonFinal
    @Value("${urlServer}")
    private String urlServer;

    public ResponseEntity callVietQRApi(CartRequest request) {
        int amount = 0;
        List<CartItemRequest> cart = request.getCart();
        for (CartItemRequest cartItem : cart) {
            Product product = productRepository
                    .findById(cartItem.getIdProduct())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
            double cartItemPrice = (1 - product.getDiscount() / 100.0) * product.getPrice() * cartItem.getCount();
            amount += cartItemPrice;
        }

        String stringAmount = String.valueOf(amount);

        QRBankRequest qrBankRequest = new QRBankRequest()
                .builder()
                .accountName(accountName)
                .accountNo(accountNo)
                .acqId(acqId)
                .amount(stringAmount) // Thay đổi giá trị này theo yêu cầu của bạn
                .template("compact")
                .addInfo("Thanh toan hoa don")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", "acbb16e7-44b2-481f-987c-7b1f07710ad6");
        headers.set("x-api-key", "ebb13b63-b1cd-45bd-b333-834a905995f3");
        headers.set("Content-Type", "application/json");

        HttpEntity<QRBankRequest> entity = new HttpEntity<>(qrBankRequest, headers);
        ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response;
    }

    public CheckoutResponseData createPaymentLink(PaymentLinkRequest paymentLinkRequest) throws Exception {
        String clientId = "90ebfb8f-a05f-495a-8c17-9e1af5eb33eb";
        String apiKey = "329c87aa-1ea2-432f-b793-185268bde7d0";
        String checkSumKey = "65d941d772bebfdde71b751803cc8a45aaa7dbd0cb8219bd0141f5fdd9fa31d3";
        String urlWebHook = "http://localhost:3000";
        PayOS payOS = new PayOS(clientId, apiKey, checkSumKey);

        SalesOrder salesOrder = salesOrderRepository
                .findById(paymentLinkRequest.getIdSalesOrder())
                .orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));

        double totalPrice = salesOrder.getCard().getTotalPrice();

        // Tao idOrder ngau nhien
        long timeStamp = System.currentTimeMillis();
        String idCreate = timeStamp + salesOrder.getIdSalesOrder().toString();
        Long idOrder = Long.valueOf(idCreate);
        PaymentData paymentData = PaymentData.builder()
                .orderCode(idOrder)
                .amount((int) totalPrice)
                .description("Thanh toan don hang")
                .returnUrl(urlWebHook + "/success")
                .cancelUrl(urlWebHook + "/cancle")
                .build();
        CheckoutResponseData checkoutResponseData = payOS.createPaymentLink(paymentData);
        payOS.confirmWebhook(urlServer+"/recieveWebhook");
        return checkoutResponseData;
    }

    public PaymentLinkData checkPaymentLink() throws Exception {
        String clientId = "90ebfb8f-a05f-495a-8c17-9e1af5eb33eb";
        String apiKey = "329c87aa-1ea2-432f-b793-185268bde7d0";
        String checkSumKey = "65d941d772bebfdde71b751803cc8a45aaa7dbd0cb8219bd0141f5fdd9fa31d3";
        PayOS payOS = new PayOS(clientId, apiKey, checkSumKey);
        PaymentLinkData paymentLinkData = payOS.getPaymentLinkInformation(123L);

        return paymentLinkData;
    }

    public ApiResponse recieveWebhook(Webhook data) {
        try {
            int idOrder = Integer.valueOf(data.getData().getOrderCode().toString().substring(13));

            SalesOrder salesOrder = salesOrderRepository.findById(idOrder).orElseThrow(() -> new AppException(ErrorCode.SALES_ORDER_NOTFOUND));
            if (salesOrder.getStatus().equals(SalesOrderStatus.PENDING_PAYMENT.name()))
            {
                salesOrder.setStatus(SalesOrderStatus.IN_PROGRESS.name());
            }
            salesOrderRepository.save(salesOrder);
        }catch (Exception e) {

        }
        return ApiResponse.builder()
                .results(data)
                .build();
    }
}
