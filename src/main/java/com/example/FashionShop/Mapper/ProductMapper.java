package com.example.FashionShop.Mapper;

import com.example.FashionShop.Dto.request.ProductCreationRequest;
import com.example.FashionShop.Dto.request.UpdateProductRequest;
import com.example.FashionShop.Entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
//    @Mapping(target = "category", ignore = true)
//    @Mapping(target = "reviews", ignore = true)
//    @Mapping(target = "cardItems", ignore = true)
//    public Product toProduct(ProductCreationRequest request);
//    public Product toUpdateProduct(@MappingTarget Product product, UpdateProductRequest request);
}
