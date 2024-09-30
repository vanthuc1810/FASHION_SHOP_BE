package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.ReviewCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Dto.response.ReviewResponse;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.Review;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IReviewService;
import com.example.FashionShop.Mapper.ReviewMapper;
import com.example.FashionShop.Repository.ProductRepository;
import com.example.FashionShop.Repository.ReviewRepository;
import com.example.FashionShop.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.flogger.Flogger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ReviewService implements IReviewService {
    ReviewRepository reviewRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ReviewMapper reviewMapper;

    @Override
    @PostAuthorize("returnObject.results.idUser == authentication.name")
    public ApiResponse<ReviewResponse> createReview(ReviewCreationRequest request)
    {
        var context = SecurityContextHolder.getContext();
        String idUser = context.getAuthentication().getName();
        User user = userRepository.findById(idUser).orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));
        Product product = productRepository.findById(request.getIdProduct()).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        LocalDate localDate = LocalDate.now();


        Review review = new Review()
                .builder()
                .star(request.getStar())
                .comment(request.getComment())
                .postedTime(localDate)
                .product(product)
                .user(user)
                .build();
        reviewRepository.save(review);

        ReviewResponse reviewResponse = new ReviewResponse()
                .builder()
                .star(request.getStar())
                .idReview(review.getIdReview())
                .comment(request.getComment())
                .idProduct(product.getIdProduct())
                .idUser(user.getIdUser())
                .postedTime(localDate)
                .build();
        return ApiResponse.<ReviewResponse>builder()
                .results(reviewResponse)
                .build();
    }

    @Override
    public ApiResponse<ReviewResponse> getReviewById(String idReview) {
        Review review = reviewRepository.findById(idReview).orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOTFOUND));
        ReviewResponse reviewResponse = new ReviewResponse()
                .builder()
                .star(review.getStar())
                .idReview(review.getIdReview())
                .comment(review.getComment())
                .idProduct(review.getProduct().getIdProduct())
                .idUser(review.getUser().getIdUser())
                .postedTime(review.getPostedTime())
                .build();
        return ApiResponse.<ReviewResponse>builder()
                .results(reviewResponse)
                .build();
    }

    @Override
    public PageableResponse getReviewByIdProduct(String idProduct, int page, int size)
    {
        Pageable pageable = PageRequest.of(page,size);

        Page<Review> listReviews = reviewRepository.findAllByIdProduct(idProduct, pageable);
        List<ReviewResponse> listReviewResponse = new ArrayList<>();
        for (Review review : listReviews.getContent())
        {
            ReviewResponse response = new ReviewResponse()
                    .builder()
                    .idReview(review.getIdReview())
                    .star(review.getStar())
                    .idProduct(review.getProduct().getIdProduct())
                    .idUser(review.getUser().getIdUser())
                    .postedTime(review.getPostedTime())
                    .comment(review.getComment())
                    .build();
            listReviewResponse.add(response);
        }

        PageableResponse pageableResponse = new PageableResponse()
                .builder()
                .totalElements(listReviews.getTotalElements())
                .totalPages(listReviews.getTotalPages())
                .results(listReviewResponse)
                .number(listReviews.getNumber())
                .size(listReviews.getSize())
                .build();
        return pageableResponse;
    }

    @Override
    public ApiResponse<List<ReviewResponse>> getReviewByIdUser(String idUser)
    {
        List<Review> listReviews = reviewRepository.findAllByIdUser(idUser);
        List<ReviewResponse> listReviewResponse = new ArrayList<>();
        for (Review review : listReviews)
        {
            ReviewResponse response = new ReviewResponse()
                    .builder()
                    .idReview(review.getIdReview())
                    .star(review.getStar())
                    .idProduct(review.getProduct().getIdProduct())
                    .idUser(review.getUser().getIdUser())
                    .postedTime(review.getPostedTime())
                    .comment(review.getComment())
                    .build();
            listReviewResponse.add(response);
        }

        return ApiResponse.<List<ReviewResponse>>builder()
                .results(listReviewResponse)
                .build();
    }
}
