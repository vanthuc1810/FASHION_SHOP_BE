package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)

public class WalletService {
    UserRepository userRepository;


    public ApiResponse getMyWallet()
    {

        return new ApiResponse().builder().build();
    }
}
