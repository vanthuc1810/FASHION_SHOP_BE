package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.Transaction;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Enum.TransactionSatus;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.ITransactionService;
import com.example.FashionShop.Repository.TransactionRepository;
import com.example.FashionShop.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.payos.type.Webhook;

import java.time.LocalDateTime;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    TransactionRepository transactionRepository;
    UserRepository userRepository;

    @Override
    public Transaction createTransaction(float amount) {
        var context = SecurityContextHolder.getContext();
        Integer idUser = Integer.parseInt(context.getAuthentication().getName());
        Transaction transaction = new Transaction()
                .builder()
                .idUser(idUser)
                .tranSactionTime(LocalDateTime.now())
                .status(TransactionSatus.PENDING.name())
                .amount(amount)
                .build();
        transactionRepository.save(transaction);
        return transaction;
    }

    @Override
    public ApiResponse recieveWebhook(Webhook data) {
        try {
            // GET TRANSATION
            Integer idOrder = Integer.parseInt(data.getData().getOrderCode().toString().substring(13));
            Transaction transaction = transactionRepository.findById(idOrder).orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOTFOUND));

            // Update User
            Integer idUser = transaction.getIdUser();
            User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOTFOUND));
            float totalAmount = user.getWallet() + data.getData().getAmount();
            user.setWallet(totalAmount);

            // Update Transaction
            transaction.setStatus(TransactionSatus.COMPLETE.name());
            transactionRepository.save(transaction);
            userRepository.save(user);
        }catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return ApiResponse.builder().results(data.getData()).build();
    }

}
