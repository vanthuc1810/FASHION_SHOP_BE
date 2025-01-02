package com.example.FashionShop.Services;

import java.time.LocalDateTime;

import com.example.FashionShop.Dto.request.QRBank.PaymentLinkRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.SalesOrder;
import com.example.FashionShop.Enum.SalesOrderStatus;
import com.example.FashionShop.IServices.IQRBankService;
import com.example.FashionShop.Repository.SalesOrderRepository;
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

    @Override
    public CheckoutResponseData createPaymentLink(PaymentLinkRequest paymentLinkRequest) throws Exception {

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
                .returnUrl(urlFE + "/success")
                .cancelUrl(urlFE + "/cancle")
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
            }
            salesOrderRepository.save(salesOrder);
        }catch (Exception e) {

        }
        return ApiResponse.builder()
                .results(data)
                .build();
    }
}
