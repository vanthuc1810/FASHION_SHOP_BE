package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.CardItemCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class CardItemService {
    CardItemRepository cardItemRepository;
    ProductRepository productRepository;
    CardRepository cardRepository;

    public CardItem createCardItem (CardItemCreationRequest request, String idCard)
    {

        Product product = productRepository.findById(request.getIdProduct()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        Card card = cardRepository.findById(idCard).orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
        CardItem cardItem = new CardItem()
                .builder()
                .price(product.getPrice())
                .quantity(request.getQuantity())
                .card(card)
                .build();
        cardItemRepository.save(cardItem);
        return cardItem;
    }
}
