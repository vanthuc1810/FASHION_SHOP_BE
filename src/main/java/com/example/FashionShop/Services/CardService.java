package com.example.FashionShop.Services;

import java.util.ArrayList;
import java.util.List;

import com.example.FashionShop.Dto.response.CardItemResponse;
import com.example.FashionShop.Dto.response.CardResponse;
import com.example.FashionShop.Dto.response.ProductResponse;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.IServices.ICardService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.FashionShop.Dto.request.CardCreationRequest;
import com.example.FashionShop.Dto.request.CardItemCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.Card;
import com.example.FashionShop.Entity.CardItem;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.CardItemRepository;
import com.example.FashionShop.Repository.CardRepository;
import com.example.FashionShop.Repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CardService implements ICardService {
    CardRepository cardRepository;
    CardItemService cardItemService;
    CardItemRepository cardItemRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public ApiResponse createCard(CardCreationRequest request) {
        float total_price = 0;
        // CREATE LIST CardItemCreationRequest
        List<CardItemCreationRequest> requests = request.getListCardItem();
        List<CardItem> listCardItems = new ArrayList<>();
        // GET INFOR USER
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();

        // CREATE CARD
        Card card = new Card();
        cardRepository.save(card);
        for (CardItemCreationRequest cardItemCreationRequest : requests) {
            CardItem cardItem = cardItemService.createCardItem(cardItemCreationRequest, card.getIdCard());
            cardItemRepository.save(cardItem);
            total_price += cardItem.getPrice() * cardItem.getQuantity();
        }
        card.setTotalPrice(total_price);
        card.setUser(userRepository.findById(Integer.parseInt(idUser)).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND)));
        cardRepository.save(card);
        return new ApiResponse<>().builder().results(card).build();
    }
    @Override
    public ApiResponse getAllCart()
    {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(Integer.parseInt(idUser)).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        return ApiResponse
                .builder()
                .results(cardRepository.findAllByIdUser(user.getIdUser()))
                .build();
    }
    @Override
    public ApiResponse getCartById(Integer idCard)
    {
        Card card = cardRepository.findById(idCard).orElseThrow(() -> new AppException(ErrorCode.CARD_NOTFOUND));
        List<CardItemResponse> listCardItemResponse = new ArrayList<>();
        double originPrice = 0;
        for(CardItem cardItem: card.getCardItems())
        {
            Product product = cardItem.getProduct();
            originPrice += product.getPrice() * cardItem.getQuantity();
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
            CardItemResponse response = CardItemResponse
                    .builder()
                    .idCardItem(cardItem.getIdCardItem())
                    .price(cardItem.getPrice())
                    .quantity(cardItem.getQuantity())
                    .color(cardItem.getColor())
                    .size(cardItem.getSize())
                    .product(productResponse)
                    .build();
            listCardItemResponse.add(response);
        }
        // get origin price

        CardResponse cardResponse = CardResponse
                .builder()
                .idCard(card.getIdCard())
                .totalPrice(card.getTotalPrice())
                .cardItems(listCardItemResponse)
                .originPrice(originPrice)
                .build();
        return ApiResponse
                .builder()
                .results(cardResponse)
                .build();
    }
}
