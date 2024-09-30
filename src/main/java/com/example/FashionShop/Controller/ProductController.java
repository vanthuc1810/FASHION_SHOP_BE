package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
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

    @GetMapping()
    public PageableResponse getAllProducts(@RequestParam(required = false) int page,
                                           @RequestParam(required = false) int size)
    {
        return productService.getAllProducts(page, size);
    }
    @GetMapping("/{idProduct}")
    public ApiResponse getProductById(@PathVariable("idProduct") String idProduct)
    {
        return productService.getProductById(idProduct);
    }

    @GetMapping("/searchProducts")
    public ApiResponse searchProducs()
    {
        return productService.searchProducts();
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

    @PutMapping("/addColor")
    public ApiResponse addColorToProduct(@RequestBody ColorCreationRequest request)
    {
        return productService.addColorToProduct(request);
    }
    @PutMapping("/addSize")
    public ApiResponse addSizeToProduct(@RequestBody SizeCreationRequest request)
    {
        return productService.addSizeToProduct(request);
    }

    @DeleteMapping("/delete/{idProduct}")
    public ApiResponse deleteProductById(@PathVariable("idProduct") String idProduct)
    {
        return productService.deleteProductById(idProduct);
    }

}

