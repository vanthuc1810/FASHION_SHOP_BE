package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.CardItemResponse;
import com.example.FashionShop.Dto.response.ProductResponse;
import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.request.CardItemCreationRequest;
import com.example.FashionShop.Entity.Card;
import com.example.FashionShop.Entity.CardItem;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.CardItemRepository;
import com.example.FashionShop.Repository.CardRepository;
import com.example.FashionShop.Repository.ProductRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CardItemService {
    CardItemRepository cardItemRepository;
    ProductRepository productRepository;
    CardRepository cardRepository;

    public CardItem createCardItem(CardItemCreationRequest request, Integer idCard) {

        Product product = productRepository
                .findById(request.getIdProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        Card card = cardRepository.findById(idCard).orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
        CardItem cardItem = new CardItem()
                .builder()
                .price(product.getPrice() * (1 - (double) product.getDiscount() / 100))
                .quantity(request.getQuantity())
                .card(card)
                .product(product)
                .build();
        cardItemRepository.save(cardItem);
        return cardItem;
    }
    public ApiResponse getCardItemById(Integer idCardItem)
    {
        CardItem cardItem = cardItemRepository.findById(idCardItem).orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
        Product product = cardItem.getProduct();
        ProductResponse productResponse = ProductResponse
                .builder()
                .idProduct(product.getIdProduct())
                .description(product.getDescription())
                .manufacturer(product.getManufacturer())
                .name(product.getName())
                .images(product.getImages())
                .discount(product.getDiscount())
                .price(product.getPrice())
                .deleted(product.isDeleted())
                .unitStock(product.getUnitStock())
                .build();
        CardItemResponse cardItemResponse = CardItemResponse
                .builder()
                .product(productResponse)
                .idCardItem(cardItem.getIdCardItem())
                .price(cardItem.getPrice())
                .quantity(cardItem.getQuantity())
                .color(cardItem.getColor())
                .size(cardItem.getSize())
                .build();
        return ApiResponse
                .builder()
                .results(cardItemResponse)
                .build();
    }

}
