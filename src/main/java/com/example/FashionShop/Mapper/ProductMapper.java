package com.example.FashionShop.Mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    //    @Mapping(target = "category", ignore = true)
    //    @Mapping(target = "reviews", ignore = true)
    //    @Mapping(target = "cardItems", ignore = true)
    //    public Product toProduct(ProductCreationRequest request);
    //    public Product toUpdateProduct(@MappingTarget Product product, UpdateProductRequest request);
}
