package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.ProductCreationRequest;
import com.example.FashionShop.Dto.request.UpdateProductRequest;
import com.example.FashionShop.Dto.response.ApiResponse;

public interface IProductService {
    public ApiResponse createProduct(ProductCreationRequest request);
    public ApiResponse getAllProducts();
    public ApiResponse getProductById(String idProduct);
    public ApiResponse updateProductById(String idProduct, UpdateProductRequest request);
    public ApiResponse deleteProductById(String idProduct);

}
