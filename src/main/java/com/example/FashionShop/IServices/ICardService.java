package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.CardCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ICardService {
    @Transactional
    ApiResponse createCard(CardCreationRequest request);

    ApiResponse getAllCart();

    ApiResponse getCartById(Integer idCard);
}
