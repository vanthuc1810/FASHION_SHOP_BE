package com.example.FashionShop.Services;

import java.util.List;

import com.example.FashionShop.Dto.request.TopUpWalletRequest;
import com.example.FashionShop.Entity.Transaction;
import com.example.FashionShop.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.request.UpdateUserRequest;
import com.example.FashionShop.Dto.request.UserCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.UserResponse;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IUserSerive;
import com.example.FashionShop.Mapper.UserMapper;
import com.example.FashionShop.Repository.UserRepository;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserService implements IUserSerive {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    TransactionService transactionService;
    TransactionRepository transactionRepository;
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
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse getUsers() {
        List<User> users = userRepository.findAll();
        return new ApiResponse<>().builder().results(users).build();
    }

    @Override
    public ApiResponse getUserById(Integer idUser) {
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        return new ApiResponse().builder().results(user).build();
    }

    @Override
    @PostAuthorize("returnObject.results.idUser == authentication.name")
    public ApiResponse getInfor() {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(Integer.parseInt(idUser)).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        UserResponse userResponse = userMapper.toUserResponse(user);
        return new ApiResponse().builder().results(userResponse).build();
    }

    @Override
    public ApiResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        userRepository.save(user);
        return new ApiResponse<>().builder().results(user).build();
    }

    @Override
    public ApiResponse updateUser(UpdateUserRequest request) {

        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(Integer.parseInt(idUser)).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        user = userMapper.updateUser(user, request);
        userRepository.save(user);
        return new ApiResponse().builder().results(user).build();
    }

    @Override
    @Transactional
    public CheckoutResponseData topUpWallet(TopUpWalletRequest request) throws Exception {
        PayOS payOS = new PayOS(clientId, apiKey, checkSumKey);

        Transaction transaction = transactionService.createTransaction(request.getTopUpWalletValue());
        // Tao idOrder ngau nhien
        long timeStamp = System.currentTimeMillis();
        String idCreate = timeStamp + transaction.getIdTransaction().toString();
        Long idOrder = Long.valueOf(idCreate);

        PaymentData paymentData = PaymentData.builder()
                .orderCode(idOrder)
                .amount((int) request.getTopUpWalletValue())
                .description("Thanh toan don hang")
                .returnUrl(urlFE + "/success")
                .cancelUrl(urlFE + "/cancle")
                .build();
        CheckoutResponseData checkoutResponseData = payOS.createPaymentLink(paymentData);

        transactionRepository.save(transaction);
        payOS.confirmWebhook(urlServer+"/transaction/walletWebhook");

        return checkoutResponseData;
    }
}
