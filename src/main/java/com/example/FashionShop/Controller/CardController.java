package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.CardCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.CardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequestMapping("/card")
public class CardController {
    CardService cardService;

    @PostMapping("/create")
    public ApiResponse createCard(@RequestBody CardCreationRequest request)
    {
        return cardService.createCard(request);
    }
}
