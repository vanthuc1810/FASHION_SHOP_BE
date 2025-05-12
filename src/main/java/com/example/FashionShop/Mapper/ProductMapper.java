package com.example.FashionShop.Mapper;

import com.example.FashionShop.Dto.request.ProductCreationRequest;
import com.example.FashionShop.Dto.request.UpdateProductRequest;
import com.example.FashionShop.Dto.response.ProductResponse;
import com.example.FashionShop.Entity.Category;
import com.example.FashionShop.Entity.ColorProduct;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.SizeProduct;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
        @Mapping(source = "request.manufacturer", target = "manufacturer")
        @Mapping(source = "request.description", target = "description")
        @Mapping(source = "request.images", target = "images")
        @Mapping(source = "request.name", target = "name")
        @Mapping(source = "request.discount", target = "discount")
        @Mapping(source = "request.price", target = "price")
        @Mapping(source = "request.unitStock", target = "unitStock")
        @Mapping(source = "request.quantity", target = "quantity")
        @Mapping(source = "category", target = "category")
        public Product toProduct(ProductCreationRequest request, Category category);

        @Mapping(source = "request.description", target = "product.description")
        @Mapping(source = "request.manufacturer", target = "product.manufacturer")
        @Mapping(source = "request.name", target = "product.name")
        @Mapping(source = "request.discount", target = "product.discount")
        @Mapping(source = "request.price", target = "product.price")
        @Mapping(source = "request.unitStock", target = "product.unitStock")
        @Mapping(target = "images", ignore = true) // bỏ qua thuộc tính imageUrl

        public Product toUpdateProduct(@MappingTarget Product product, @Valid UpdateProductRequest request);
        @Mapping(target = "colors", expression = "java(getColorNames(product))")
        @Mapping(target = "sizes", expression = "java(getSizeNames(product))")
        @Mapping(target = "idCategory", expression = "java(product.getCategory().getIdCategory())")

        public ProductResponse toProductResponse(Product product);

        public default List<String> getColorNames(Product product) {
                List<String> colors = new ArrayList<>();
                for (ColorProduct colorProduct : product.getColorProducts())
                {
                        colors.add(colorProduct.getColor().getNameColor());
                }
                return colors;
        }

        public default List<String> getSizeNames(Product product) {
                List<String> sizes = new ArrayList<>();
                for (SizeProduct sizeProduct : product.getSizeProducts())
                {
                        sizes.add(sizeProduct.getSize().getNameSize());
                }
                return sizes;
        }
        public default Category getCategory(Integer idCategory)
        {
                CategoryRepository categoryRepository = null;
                return categoryRepository.findById(idCategory).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));
        }
}
