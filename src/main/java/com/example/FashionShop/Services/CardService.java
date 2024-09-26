package com.example.FashionShop.Services;

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
import lombok.experimental.NonFinal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CardService {
    CardRepository cardRepository;
    CardItemService cardItemService;
    CardItemRepository cardItemRepository;
    UserRepository userRepository;

    @NonFinal
    double total_price = 0;

    public ApiResponse createCard(CardCreationRequest request)
    {
    // CREATE LIST CardItemCreationRequest
        List<CardItemCreationRequest> requests = request.getListCardItem();
        List<CardItem> listCardItems = new ArrayList<>();
    // GET INFOR USER
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();

    // CREATE CARD
        Card card = new Card();
        cardRepository.save(card);
        for (CardItemCreationRequest cardItemCreationRequest : requests)
        {
            CardItem cardItem = cardItemService.createCardItem(cardItemCreationRequest, card.getIdCard());
            cardItemRepository.save(cardItem);
            total_price += cardItem.getPrice() * cardItem.getQuantity();
        }
        card.setTotalPrice(total_price);
        card.setUser(userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND)));
        cardRepository.save(card);
        return new ApiResponse<>()
                .builder()
                .results(card)
                .build();
    }

}
