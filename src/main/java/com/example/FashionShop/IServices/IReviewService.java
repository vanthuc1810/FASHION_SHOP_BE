package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.ReviewCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import org.springframework.data.domain.Pageable;

public interface IReviewService {
    public ApiResponse createReview(ReviewCreationRequest request);

    public ApiResponse getReviewById(Integer idReview);

    public PageableResponse getReviewByIdProduct(Integer idProduct, Integer idUser, Pageable pageable);

}
