package com.example.FashionShop.Controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.request.CardCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.CardService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/card")
public class CardController {
    CardService cardService;

    @GetMapping()
    public ApiResponse getAllCart()
    {
        return cardService.getAllCart();
    }

    @GetMapping("/{idCard}")
    public ApiResponse getCardById(@PathVariable("idCard") Integer idCard)
    {
        return cardService.getCartById(idCard);
    }
    @PostMapping("/create")
    public ApiResponse createCard(@RequestBody @Valid CardCreationRequest request) {
        return cardService.createCard(request);
    }
}
