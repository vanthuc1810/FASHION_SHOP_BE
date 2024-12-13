package com.example.FashionShop.Controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Services.ProductService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/product")
public class ProductController {
    ProductService productService;

    @GetMapping()
    public PageableResponse getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping("/{idProduct}")
    public ApiResponse getProductById(@PathVariable("idProduct") Integer idProduct) {
        return productService.getProductById(idProduct);
    }

    @GetMapping("/searchProducts")
    public PageableResponse searchProducts(@RequestParam String query, Pageable pageable) {
        return productService.searchProducts(query, pageable);
    }

    @PostMapping("/filterProduct")
    public PageableResponse filterProducts(@RequestBody @Valid FilterProductRequest request, Pageable pageable) {
        return productService.filterProducts(request, pageable);
    }

    @PostMapping("/create")
    public ApiResponse createProduct(@RequestBody @Valid ProductCreationRequest request) {
        return productService.createProduct(request);
    }

    @PutMapping("/update/{idProduct}")
    public ApiResponse updateProduct(
            @PathVariable("idProduct") Integer idProduct, @RequestBody @Valid UpdateProductRequest request) {
        return productService.updateProductById(idProduct, request);
    }

    @PutMapping("/addColor")
    public ApiResponse addColorToProduct(@RequestBody @Valid ColorCreationRequest request) {
        return productService.addColorToProduct(request);
    }

    @PutMapping("/addSize")
    public ApiResponse addSizeToProduct(@RequestBody @Valid SizeCreationRequest request) {
        return productService.addSizeToProduct(request);
    }

    @DeleteMapping("/delete/{idProduct}")
    public ApiResponse deleteProductById(@PathVariable("idProduct") Integer idProduct) {
        return productService.deleteProductById(idProduct);
    }

    @GetMapping("/test")
    public ApiResponse test(){
        return productService.test();
    }

}
