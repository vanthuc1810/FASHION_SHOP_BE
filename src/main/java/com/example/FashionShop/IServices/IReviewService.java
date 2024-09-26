package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.ReviewCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.ReviewResponse;

public interface IReviewService {
    public ApiResponse createReview(ReviewCreationRequest request);
    public ApiResponse getReviewById(String idReview);
    public ApiResponse getReviewByIdProduct(String idProduct);
    public ApiResponse getReviewByIdUser(String idUser);
}
