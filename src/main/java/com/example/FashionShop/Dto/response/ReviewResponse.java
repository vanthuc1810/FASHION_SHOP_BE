package com.example.FashionShop.Dto.response;

import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReviewResponse {
    String idReview;
    String comment;
    int star;
    LocalDate postedTime;
    String idUser;
    String idProduct;
}
