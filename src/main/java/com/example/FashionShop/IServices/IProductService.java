package com.example.FashionShop.IServices;

import org.springframework.data.domain.Pageable;

import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;

public interface IProductService {
    public ApiResponse createProduct(ProductCreationRequest request);

    public ApiResponse addColorToProduct(ColorCreationRequest request);

    public ApiResponse addSizeToProduct(SizeCreationRequest request);

    public PageableResponse getAllProducts(Pageable pageable);

    public ApiResponse getProductById(Integer idProduct);

    public ApiResponse getAllManufacturer();

    public ApiResponse updateProductById(UpdateProductRequest request) throws IOException;

    public ApiResponse deleteProduct(DeleteProductRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse activeProduct(ActiveProductRequest request);

    public PageableResponse filterProducts(String query, FilterProductRequest request, Pageable pageable);

    PageableResponse getRecommentProduct(FilterProductRequest request, Pageable pageable);

    public PageableResponse searchProducts(String query, Pageable pageable);

    public ApiResponse getManufracture();

}
