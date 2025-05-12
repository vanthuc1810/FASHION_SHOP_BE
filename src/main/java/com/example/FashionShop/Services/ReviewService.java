package com.example.FashionShop.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.example.FashionShop.Entity.*;
import com.example.FashionShop.Repository.SalesOrderRepository;
import com.example.FashionShop.Specification.ReviewSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.request.ReviewCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Dto.response.ReviewResponse;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IReviewService;
import com.example.FashionShop.Mapper.ReviewMapper;
import com.example.FashionShop.Repository.ProductRepository;
import com.example.FashionShop.Repository.ReviewRepository;
import com.example.FashionShop.Repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    ReviewRepository reviewRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ReviewMapper reviewMapper;
    SalesOrderRepository salesOrderRepository;
    @Override
    public ApiResponse<ReviewResponse> createReview(ReviewCreationRequest request) {

//              Get Product
        Product product = productRepository.findById(request.getIdProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
//      Get User
        Integer idUser = Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getName());
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        Review review = reviewMapper.toReview(request, product, user);
        review = reviewRepository.save(review);
        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);
        return ApiResponse.<ReviewResponse>builder().results(reviewResponse).build();
    }

    @Override
    public ApiResponse<ReviewResponse> getReviewById(Integer idReview) {
        Review review =
                reviewRepository.findById(idReview).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOTFOUND));
        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);
        return ApiResponse.<ReviewResponse>builder().results(reviewResponse).build();
    }

    @Override
    public PageableResponse getReviewByIdProduct(Integer idProduct, Integer idUser, Pageable pageable) {
        Specification<Review> hasIdProduct = ReviewSpecification.hasIdProduct(idProduct);
        Specification<Review> hasIdUser = ReviewSpecification.hasIdUser(idUser);
        Specification<Review> spec = hasIdUser.and(hasIdProduct);

        Page<Review> listReviews = reviewRepository.findAll(spec, pageable);
        List<ReviewResponse> listReviewResponse = new ArrayList<>();
        for (Review review : listReviews.getContent()) {
            ReviewResponse response = reviewMapper.toReviewResponse(review);
            listReviewResponse.add(response);
        }

        return PageableResponse
                .builder()
                .totalElements(listReviews.getTotalElements())
                .totalPages(listReviews.getTotalPages())
                .results(listReviewResponse)
                .number(listReviews.getNumber())
                .size(listReviews.getSize())
                .build();
    }
}
