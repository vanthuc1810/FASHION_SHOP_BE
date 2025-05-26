package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.Transaction;
import com.example.FashionShop.IServices.ITransactionService;
import com.example.FashionShop.Services.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.type.Webhook;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/transaction")
public class TransactionController {
    ITransactionService transactionService;

    @PostMapping("/walletWebhook")
    public ApiResponse topUpWalletWebhook(@RequestBody Webhook data) throws Exception {
        return transactionService.recieveWebhook(data);
    }


}
