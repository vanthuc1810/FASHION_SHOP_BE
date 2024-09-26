package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.ReviewCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    ReviewService reviewService;

    @PostMapping("/create-review")
    public ApiResponse createReview(@RequestBody ReviewCreationRequest request)
    {
        return reviewService.createReview(request);
    }

    @GetMapping("/get-review/{idReview}")
    public ApiResponse getReviewById(@PathVariable("idReview") String idReview)
    {
        return reviewService.getReviewById(idReview);
    }

    @GetMapping("/get-review-by-idproduct/{idProduct}")
    public ApiResponse getReviewByIdProduct(@PathVariable("idProduct") String idProduct)
    {
        return reviewService.getReviewByIdProduct(idProduct);
    }

    @GetMapping("/get-review-by-iduser/{idUser}")
    public ApiResponse getReviewByIdUser(@PathVariable("idUser") String idUser)
    {
        return reviewService.getReviewByIdUser(idUser);
    }
}
