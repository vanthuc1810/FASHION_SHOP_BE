package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.ColorCreationRequest;
import com.example.FashionShop.Dto.request.ProductCreationRequest;
import com.example.FashionShop.Dto.request.SizeCreationRequest;
import com.example.FashionShop.Dto.request.UpdateProductRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;

public interface IProductService {
    public ApiResponse createProduct(ProductCreationRequest request);
    public ApiResponse addColorToProduct(ColorCreationRequest request);
    public ApiResponse addSizeToProduct(SizeCreationRequest request);
    public PageableResponse getAllProducts(int page, int size);
    public ApiResponse getProductById(String idProduct);
    public ApiResponse updateProductById(String idProduct, UpdateProductRequest request);
    public ApiResponse deleteProductById(String idProduct);
    public ApiResponse searchProducts();
}
