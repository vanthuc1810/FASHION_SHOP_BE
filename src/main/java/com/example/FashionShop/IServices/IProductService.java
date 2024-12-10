package com.example.FashionShop.IServices;

import org.springframework.data.domain.Pageable;

import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;

public interface IProductService {
    public ApiResponse createProduct(ProductCreationRequest request);

    public ApiResponse addColorToProduct(ColorCreationRequest request);

    public ApiResponse addSizeToProduct(SizeCreationRequest request);

    public PageableResponse getAllProducts(Pageable pageable);

    public ApiResponse getProductById(Integer idProduct);

    public ApiResponse getAllManufacturer();

    public ApiResponse updateProductById(Integer idProduct, UpdateProductRequest request);

    public ApiResponse deleteProductById(Integer idProduct);

    public PageableResponse filterProducts(FilterProductRequest request, Pageable pageable);

    public PageableResponse searchProducts(String query, Pageable pageable);
}
