package com.example.FashionShop.Mapper;

import com.example.FashionShop.Dto.response.CardItemResponse;
import com.example.FashionShop.Dto.response.CardResponse;
import com.example.FashionShop.Entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "cardItems", source = "cardItemResponses")
    @Mapping(target = "originPrice", source = "originPrice")
    CardResponse toCardResponse(Card card, List<CardItemResponse> cardItemResponses, double originPrice);
}
