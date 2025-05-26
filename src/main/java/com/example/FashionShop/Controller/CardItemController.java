package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.IServices.ICardItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.FashionShop.Services.CardItemService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/cardItem")
public class CardItemController {
    ICardItemService cardItemService;

    @GetMapping("/{idCardItem}")
    public ApiResponse getCardItemById(@PathVariable("idCardItem") Integer idCardItem)
    {
        return cardItemService.getCardItemById(idCardItem);
    }
}
