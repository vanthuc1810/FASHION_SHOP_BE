package com.example.FashionShop.Dto.response;

import java.time.LocalDate;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReviewResponse {
    Integer idReview;
    String comment;
    int star;
    LocalDate postedTime;
    Integer idUser;
    Integer idProduct;
}
