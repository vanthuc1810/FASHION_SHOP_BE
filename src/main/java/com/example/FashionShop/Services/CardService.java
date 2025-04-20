package com.example.FashionShop.Services;

import java.util.ArrayList;
import java.util.List;

import com.example.FashionShop.Dto.response.CardItemResponse;
import com.example.FashionShop.Dto.response.CardResponse;
import com.example.FashionShop.Dto.response.ProductResponse;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.IServices.ICardService;
import com.example.FashionShop.Mapper.CardItemMapper;
import com.example.FashionShop.Mapper.CardMapper;
import com.example.FashionShop.Mapper.ProductMapper;
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
    ProductMapper productMapper;
    CardItemMapper cardItemMapper;
    CardMapper cardMapper;

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

            ProductResponse productResponse = productMapper.toProductResponse(product);
            CardItemResponse response = cardItemMapper.toCardItemResponse(cardItem, productResponse);
            listCardItemResponse.add(response);
        }
        CardResponse cardResponse = cardMapper.toCardResponse(card, listCardItemResponse, originPrice);
        return ApiResponse
                .builder()
                .results(cardResponse)
                .build();
    }
}
