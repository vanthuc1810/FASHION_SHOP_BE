package com.example.FashionShop.Services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.FashionShop.Dto.response.ProductResponse;
import com.example.FashionShop.Entity.*;
import com.example.FashionShop.Specification.ProductSpecification;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IProductService;
import com.example.FashionShop.Mapper.ProductMapper;
import com.example.FashionShop.Repository.CategoryRepository;
import com.example.FashionShop.Repository.ColorRepository;
import com.example.FashionShop.Repository.ProductRepository;
import com.example.FashionShop.Repository.SizeRepository;

import lombok.*;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Builder
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService{
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    ColorRepository colorRepository;
    SizeRepository sizeRepository;

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
                .category(categoryRepository
                        .findById(request.getIdCategory())
                        .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND)))
                .build();
        productRepository.save(product);
        return new ApiResponse().builder().results(product).build();
    }

    @Override
    public ApiResponse addColorToProduct(ColorCreationRequest request) {
        Product product = productRepository
                .findById(request.getIdProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        // 2. Tìm hoặc tạo mới màu sắc theo nameColor
        List<ColorProduct> productColors = null; // Lấy danh sách màu của sản phẩm
        productColors = product.getColorProducts();

        for (String nameColor : request.getColors()) {
            Color color = colorRepository.findById(nameColor).orElseGet(() -> {
                // Nếu không tìm thấy màu sắc, tạo mới
                Color newColor = new Color();
                newColor.setNameColor(nameColor);
                return colorRepository.save(newColor); // Lưu màu sắc mới vào database
            });
            ColorProduct colorProduct = new ColorProduct()
                    .builder()
                    .product(product)
                    .color(color)
                    .build();

            if (!productColors.contains(colorProduct)) {
                productColors.add(colorProduct);
                product.setColorProducts(productColors); // Cập nhật danh sách màu cho sản phẩm
            }
        }

        // 3. Luu san pham
        productRepository.save(product);

        return new ApiResponse().builder().results(product).build();
    }

    @Override
    public ApiResponse addSizeToProduct(SizeCreationRequest request) {
//         GET PRODUCT BY ID
        Product product = productRepository
                .findById(request.getIdProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));

        // Tim hoac tao moi Size roi add vao list size trong product
        List<SizeProduct> listSizeProduct = product.getSizeProducts();
        for (String nameSize : request.getSizes()) {
            Size size = sizeRepository.findById(nameSize).orElseGet(() -> {
                Size newSize = new Size().builder().nameSize(nameSize).build();
                return sizeRepository.save(newSize);
            });
            SizeProduct sizeProduct = SizeProduct
                    .builder()
                    .product(product)
                    .size(size)
                    .build();
            if (!listSizeProduct.contains(sizeProduct)) {
                listSizeProduct.add(sizeProduct);
                product.setSizeProducts(listSizeProduct);
            }
        }
        productRepository.save(product);
        return new ApiResponse().builder().results(product).build();
    }

    @Override
    public PageableResponse getAllProducts(Pageable pageable) {
        Page pageOrigin = productRepository.findAll(pageable);

        // set dl vao dto response
        PageableResponse pageableResponse = new PageableResponse()
                .builder()
                .results(pageOrigin.getContent())
                .size(pageOrigin.getSize())
                .totalElements(pageOrigin.getTotalElements())
                .totalPages(pageOrigin.getTotalPages())
                .number(pageOrigin.getNumber())
                .build();

        return pageableResponse;
    }

    @Override
    public ApiResponse getProductById(Integer idProduct) {
        Product product = productRepository.findById(idProduct).orElseThrow(() -> new AppException());
        List<String> colors = new ArrayList<>();
        List<String> sizes = new ArrayList<>();
        for (ColorProduct colorProduct : product.getColorProducts())
        {
            colors.add(colorProduct.getColor().getNameColor());
        }

        for (SizeProduct sizeProduct : product.getSizeProducts())
        {
            sizes.add(sizeProduct.getSize().getNameSize());
        }
        ProductResponse productResponse = ProductResponse
                .builder()
                .idProduct(product.getIdProduct())
                .description(product.getDescription())
                .manufacturer(product.getManufacturer())
                .name(product.getName())
                .images(product.getImages())
                .discount(product.getDiscount())
                .price(product.getPrice())
                .deleted(product.isDeleted())
                .unitStock(product.getUnitStock())
                .colors(colors)
                .sizes(sizes)
                .build();
        return new ApiResponse().builder().results(productResponse).build();
    }

    @Override
    public ApiResponse getAllManufacturer() {
        List<String> listManufacturer = productRepository.getAllManufacturer();
        return new ApiResponse().builder().results(listManufacturer).build();
    }

    @Override
    public ApiResponse updateProductById(Integer idProduct, UpdateProductRequest request) {
        Product product =
                productRepository.findById(idProduct).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
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
    public ApiResponse deleteProductById(Integer idProduct) {
        Product product = productRepository.findById(idProduct).orElseThrow(() -> new AppException());
        product.setDeleted(true);
        productRepository.delete(product);
        return new ApiResponse().builder().results(product).build();
    }

    @Override
    public PageableResponse filterProducts(FilterProductRequest request, Pageable pageable) {
        Long minPrice = request.getPrices().get(0);
        Long maxPrice = request.getPrices().get(1);
        String manufacturer = request.getManufacturer();
        int idCategory = request.getIdCategory();
        List<String> colors = request.getColors();
        List<String> sizes = request.getSizes();
        
        // filter Manufacturer - Price - Colors - Size - Category
        Specification<Product> spec = Specification.where(ProductSpecification.hasManufacturer(manufacturer))
                .and(ProductSpecification.hasColors(colors))
                .and(ProductSpecification.hasSizes(sizes))
                .and(ProductSpecification.hasPriceInRange(minPrice,maxPrice))
                .and(ProductSpecification.hasIdCategory(idCategory));

        Page<Product> pageOrigin = productRepository.findAll(spec,pageable);
        return PageableResponse
                .builder()
                .results(pageOrigin.getContent())
                .size(pageOrigin.getSize())
                .totalElements(pageOrigin.getTotalElements())
                .totalPages(pageOrigin.getTotalPages())
                .number(pageOrigin.getNumber())
                .build();
    }

    @Override
    public PageableResponse searchProducts(String keyword, Pageable pageable) {
        Specification<Product> spec = Specification.where(null);

        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = Specification.where(ProductSpecification
                    .hasName(keyword))
                    .or(ProductSpecification.hasDescription(keyword));
        }

        Page<Product> listProducts = productRepository.findAll(spec, pageable);
        return PageableResponse.builder()
                .results(listProducts.getContent())
                .size(listProducts.getSize())
                .totalElements(listProducts.getTotalElements())
                .totalPages(listProducts.getTotalPages())
                .number(listProducts.getNumber())
                .build();
    }

    public ApiResponse test()
    {
        List<String> listColors = Arrays.asList("Medium", "#123");
        Specification<Product> spec = Specification
                .where(ProductSpecification.hasSizes(listColors));
        List<Product> listProduct = productRepository.findAll(spec);
        return ApiResponse.builder().results(listProduct).build();
    }

}
