package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.ProductCreationRequest;
import com.example.FashionShop.Dto.request.UpdateProductRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IProductService;
import com.example.FashionShop.Mapper.ProductMapper;
import com.example.FashionShop.Repository.CategoryRepository;
import com.example.FashionShop.Repository.ProductRepository;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Builder
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ProductService implements IProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;

    @Override
    public ApiResponse createProduct(ProductCreationRequest request) {
        Product product = new Product()
                .builder()
                .description(request.getDescription())
                .manufacturer(request.getManufacturer())
                .name(request.getName())
                .images(request.getImages())
                .discount(request.getDiscount())
                .price(request.getPrice())
                .unitStock(request.getUnitStock())
                .category(categoryRepository.findById(request.getIdCategory()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND)))
                .build();
        productRepository.save(product);
        return new ApiResponse()
                .builder()
                .results(product)
                .build();
    }

    @Override
    public ApiResponse getAllProducts() {
        Sort sort = Sort.by(Sort.Order.asc("name"));
        return new ApiResponse()
                .builder()
                .results(productRepository.findAll(sort))
                .build();
    }

    @Override
    public ApiResponse getProductById(String idProduct) {
        Product product = productRepository.findById(idProduct).orElseThrow(() -> new AppException());
        return new ApiResponse().builder().results(product).build();
    }

    @Override
    public ApiResponse updateProductById(String idProduct, UpdateProductRequest request)
    {
        Product product = productRepository.findById(idProduct).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        product.setDescription(request.getDescription());
        product.setManufacturer(request.getManufacturer());
        product.setName(request.getName());
        product.setDiscount(request.getDiscount());
        product.setPrice(request.getPrice());
        product.setUnitStock(request.getUnitStock());
        productRepository.save(product);
        return new ApiResponse().builder().results(product).build();
    }

    @Override
    public ApiResponse deleteProductById(String idProduct) {
        Product product = productRepository.findById(idProduct).orElseThrow(() -> new AppException());
        product.setDeleted(true);
        productRepository.save(product);
        return new ApiResponse().builder().results(product).build();
    }
}