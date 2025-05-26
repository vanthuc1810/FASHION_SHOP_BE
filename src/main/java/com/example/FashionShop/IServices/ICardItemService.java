package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.CardItemCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.CardItem;

public interface ICardItemService {
    CardItem createCardItem(CardItemCreationRequest request, Integer idCard);

    ApiResponse getCardItemById(Integer idCardItem);
}
