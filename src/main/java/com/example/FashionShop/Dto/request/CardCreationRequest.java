package com.example.FashionShop.Dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardCreationRequest {
    @NotEmpty(message = "CARD_NOT_EMPTY")
    List<CardItemCreationRequest> listCardItem;
}
