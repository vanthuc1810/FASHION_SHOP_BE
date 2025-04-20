package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.CardItemResponse;
import com.example.FashionShop.Dto.response.ProductResponse;
import com.example.FashionShop.Mapper.CardItemMapper;
import com.example.FashionShop.Mapper.ProductMapper;
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
    ProductMapper productMapper;
    CardItemMapper cardItemMapper;
    public CardItem createCardItem(CardItemCreationRequest request, Integer idCard) {

        Product product = productRepository
                .findById(request.getIdProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        if(request.getQuantity() > product.getQuantity())
        {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        Card card = cardRepository.findById(idCard).orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
        Integer newQuantity = product.getQuantity() - request.getQuantity();
        product.setQuantity(newQuantity);
        CardItem cardItem = cardItemMapper.toCardItem(request, product, card);
        cardItemRepository.save(cardItem);
        productRepository.save(product);
        return cardItem;
    }
    public ApiResponse getCardItemById(Integer idCardItem)
    {
        CardItem cardItem = cardItemRepository.findById(idCardItem).orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
        Product product = cardItem.getProduct();
        ProductResponse productResponse = productMapper.toProductResponse(product);
        CardItemResponse cardItemResponse = cardItemMapper.toCardItemResponse(cardItem, productResponse);
        return ApiResponse
                .builder()
                .results(cardItemResponse)
                .build();
    }

}
