package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.CardItemCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.CardItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)

public class CardItemController {
    CardItemService cardItemService;

//    @PostMapping("/create-card-item")
//    public ApiResponse createCardItem(@RequestBody CardItemCreationRequest request)
//    {
//        return cardItemService.createCardItem(request);
//    }

}
