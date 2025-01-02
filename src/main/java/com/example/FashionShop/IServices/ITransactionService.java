package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.Transaction;
import vn.payos.type.Webhook;

public interface ITransactionService {
    Transaction createTransaction(float amount);
    ApiResponse recieveWebhook(Webhook data);
}
