package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.ReviewCreationRequest;
import com.example.FashionShop.Dto.request.SizeCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Dto.response.ReviewResponse;
import com.example.FashionShop.Dto.response.SizeResponse;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.Review;
import com.example.FashionShop.Entity.User;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IReviewService;
import com.example.FashionShop.IServices.ISizeService;
import com.example.FashionShop.Mapper.ReviewMapper;
import com.example.FashionShop.Repository.ProductRepository;
import com.example.FashionShop.Repository.ReviewRepository;
import com.example.FashionShop.Repository.SizeRepository;
import com.example.FashionShop.Repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SizeService implements ISizeService {
    SizeRepository sizeRepository;

    @Override
    public ApiResponse<SizeResponse> createSize(SizeCreationRequest request)
    {
        return null;
    }
    @Override
    public ApiResponse<SizeResponse> getSize(String nameSize)
    {
        return  null;
    }

    @Override
    public ApiResponse getAllSize() {
        List<String> listSize = sizeRepository.findAllSize();
        return new ApiResponse()
                .builder()
                .results(listSize)
                .build();
    }

}
