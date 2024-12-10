package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.QRBank.PaymentLinkRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.request.QRBank.CartRequest;
import com.example.FashionShop.Services.QRBankService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentLinkData;
import vn.payos.type.Webhook;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QRBankController {
    QRBankService qrBankService;

    @PostMapping("/createQrCode")
    public ResponseEntity createQrCode(@RequestBody CartRequest request) {

        return qrBankService.callVietQRApi(request);
    }

    @PostMapping("/recieveWebhook")
    public ApiResponse recieveWebhook(@RequestBody Webhook data) throws Exception {
        return qrBankService.recieveWebhook(data);
    }


    @PostMapping("/createPaymentLink")
    public CheckoutResponseData createPaymentLink(@RequestBody PaymentLinkRequest paymentLinkRequest) throws Exception {
        return qrBankService.createPaymentLink(paymentLinkRequest);
    }

    @PostMapping("/checkPaymentLink")
    public PaymentLinkData checkPaymentLink() throws Exception {
        return qrBankService.checkPaymentLink();
    }
}
