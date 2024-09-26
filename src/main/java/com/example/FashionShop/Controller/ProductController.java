package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.ProductCreationRequest;
import com.example.FashionShop.Dto.request.UpdateProductRequest;
import com.example.FashionShop.Dto.request.UpdateUserRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.ProductService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/product")

public class ProductController {
    ProductService productService;

    @GetMapping("")
    public ApiResponse getAllProducts()
    {
        return productService.getAllProducts();
    }
    @GetMapping("/{idProduct}")
    public ApiResponse getProductById(@PathVariable("idProduct") String idProduct)
    {
        return productService.getProductById(idProduct);
    }

    @PostMapping("/create")
    public ApiResponse createProduct(@RequestBody ProductCreationRequest request)
    {
        return productService.createProduct(request);
    }

    @PutMapping("/update/{idProduct}")
    public ApiResponse updateProduct(@PathVariable("idProduct") String idProduct, @RequestBody UpdateProductRequest request)
    {
        return productService.updateProductById(idProduct, request);
    }

    @DeleteMapping("/delete/{idProduct}")
    public ApiResponse deleteProductById(@PathVariable("idProduct") String idProduct)
    {
        return productService.deleteProductById(idProduct);
    }

}

