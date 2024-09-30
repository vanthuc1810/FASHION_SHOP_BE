package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.ColorCreationRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.ColorResponse;
import com.example.FashionShop.Entity.Color;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IColorService;
import com.example.FashionShop.Repository.ColorRepository;
import com.example.FashionShop.Repository.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ColorService implements IColorService {
    ColorRepository colorRepository;
    ProductRepository productRepository;


    @Override
    public ApiResponse createColor(ColorCreationRequest request) {
        return null;
    }

    @Override
    public ApiResponse getColor(String nameColor) {
        return null;
    }

    @Override
    public ApiResponse getAllColor() {
        List<String> listColor = colorRepository.findAllColor();
        return new ApiResponse()
                .builder()
                .results(listColor)
                .build();
    }
}
