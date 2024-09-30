package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.ReviewCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Dto.response.ReviewResponse;

public interface IReviewService {
    public ApiResponse createReview(ReviewCreationRequest request);
    public ApiResponse getReviewById(String idReview);
    public PageableResponse getReviewByIdProduct(String idProduct, int page, int size);
    public ApiResponse getReviewByIdUser(String idUser);
}
