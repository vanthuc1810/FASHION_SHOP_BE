package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.response.ReviewResponse;
import com.example.FashionShop.IServices.IReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.request.ReviewCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Services.ReviewService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    IReviewService reviewService;

    @PostMapping("/create")
    public ApiResponse<ReviewResponse> createReview(@RequestBody @Valid ReviewCreationRequest request) {
        return reviewService.createReview(request);
    }

    @GetMapping("/{idReview}")
    public ApiResponse<ReviewResponse> getReviewById(@PathVariable("idReview") Integer idReview) {
        return reviewService.getReviewById(idReview);
    }

    @GetMapping("")
    public PageableResponse getReviewByIdProduct(
            @RequestParam(value = "idProduct", required = false) Integer idProduct,
            @RequestParam(value = "idUser", required = false) Integer idUser,
            Pageable pageable) {
        return reviewService.getReviewByIdProduct(idProduct, idUser, pageable);
    }
}
