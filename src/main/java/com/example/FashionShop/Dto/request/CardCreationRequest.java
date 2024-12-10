package com.example.FashionShop.Dto.request;

import java.util.List;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CardCreationRequest {
    List<CardItemCreationRequest> listCardItem;
}
