package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.QRBank.PaymentLinkRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;

public interface IQRBankService {
    CheckoutResponseData createPaymentLink(PaymentLinkRequest paymentLinkRequest) throws Exception;

    PaymentLinkData checkPaymentLink(Long idOrder) throws Exception;

    ApiResponse recieveWebhook(Webhook data);
}
