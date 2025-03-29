package com.example.FashionShop.Mapper;

import com.example.FashionShop.Dto.request.ReviewCreationRequest;
import com.example.FashionShop.Dto.response.ReviewResponse;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.Review;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.ProductRepository;
import com.example.FashionShop.Repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;

@Mapper(componentModel = "spring", imports = {LocalDate.class})
public interface ReviewMapper {
    @Mapping(expression = "java(review.getUser().getIdUser())", target = "idUser")
    @Mapping(expression = "java(review.getProduct().getIdProduct())", target = "idProduct")
    ReviewResponse toReviewResponse(Review review);

    @Mapping(source = "request.star", target = "star")
    @Mapping(source = "request.comment", target = "comment")
    @Mapping(expression = "java(LocalDate.now())", target = "postedTime")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "user", target = "user")
    Review toReview(ReviewCreationRequest request, Product product, User user);

}
