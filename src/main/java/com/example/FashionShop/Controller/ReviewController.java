package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.response.ReviewResponse;
import jakarta.validation.Valid;
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
    ReviewService reviewService;

    @PostMapping("/create")
    public ApiResponse<ReviewResponse> createReview(@RequestBody @Valid ReviewCreationRequest request) {
        return reviewService.createReview(request);
    }

    @GetMapping("/get-review/{idReview}")
    public ApiResponse<ReviewResponse> getReviewById(@PathVariable("idReview") Integer idReview) {
        return reviewService.getReviewById(idReview);
    }

    @GetMapping("/get-review-by-idproduct/{idProduct}")
    public PageableResponse getReviewByIdProduct(
            @PathVariable("idProduct") Integer idProduct, @RequestParam int page, @RequestParam int size) {
        return reviewService.getReviewByIdProduct(idProduct, page, size);
    }

    @GetMapping("/get-review-by-iduser/{idUser}")
    public ApiResponse getReviewByIdUser(@PathVariable("idUser") Integer idUser) {
        return reviewService.getReviewByIdUser(idUser);
    }
}
