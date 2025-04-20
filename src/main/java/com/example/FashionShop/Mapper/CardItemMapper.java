package com.example.FashionShop.Mapper;

import com.example.FashionShop.Dto.request.CardItemCreationRequest;
import com.example.FashionShop.Dto.response.CardItemResponse;
import com.example.FashionShop.Dto.response.ProductResponse;
import com.example.FashionShop.Entity.Card;
import com.example.FashionShop.Entity.CardItem;
import com.example.FashionShop.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardItemMapper {
    @Mapping(target = "product", source = "productResponse") // Map ProductResponse vào field 'product'
    @Mapping(target = "idCardItem", source = "cardItem.idCardItem")
    @Mapping(target = "price", source = "cardItem.price")
    @Mapping(target = "quantity", source = "cardItem.quantity")
    @Mapping(target = "color", source = "cardItem.color")
    @Mapping(target = "size", source = "cardItem.size")
    CardItemResponse toCardItemResponse(CardItem cardItem, ProductResponse productResponse);
    @Mapping(target = "price", expression = "java(product.getPrice() * (1 - (double) product.getDiscount() / 100))")
    @Mapping(target = "product", source = "product")
    @Mapping(target = "card", source = "card")
    @Mapping(target = "quantity", source = "request.quantity")

    CardItem toCardItem(CardItemCreationRequest request, Product product, Card card);
}
