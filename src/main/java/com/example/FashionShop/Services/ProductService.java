package com.example.FashionShop.Services;

import com.example.FashionShop.Dto.request.ColorCreationRequest;
import com.example.FashionShop.Dto.request.ProductCreationRequest;
import com.example.FashionShop.Dto.request.SizeCreationRequest;
import com.example.FashionShop.Dto.request.UpdateProductRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Entity.Color;
import com.example.FashionShop.Entity.Product;
import com.example.FashionShop.Entity.Size;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Builder
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class ProductService implements IProductService {
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
//                .manufacturer(request.getManufacturer())
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
    public ApiResponse addColorToProduct(ColorCreationRequest request) {
        Product product = productRepository.findById(request.getIdProducts())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        // 2. Tìm hoặc tạo mới màu sắc theo nameColor
        List<Color> productColors = null;  // Lấy danh sách màu của sản phẩm
        productColors = product.getColors();
        for(String nameColor : request.getNameColor())
        {
            Color color = colorRepository.findById(nameColor)
                    .orElseGet(() -> {
                        // Nếu không tìm thấy màu sắc, tạo mới
                        Color newColor = new Color();
                        newColor.setNameColor(nameColor);
                        return colorRepository.save(newColor);  // Lưu màu sắc mới vào database
                    });
            if(!productColors.contains(color))
            {
                productColors.add(color);
                product.setColors(productColors);  // Cập nhật danh sách màu cho sản phẩm
            }
        }

        // 3. Luu san pham
        productRepository.save(product);

        return new ApiResponse()
                .builder()
                .results(product)
                .build();
    }

    @Override
    public ApiResponse addSizeToProduct(SizeCreationRequest request) {
        // GET PRODUCT BY ID
        Product product = productRepository.findById(request.getIdProducts())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));

        // Tim hoac tao moi Size roi add vao list size trong product
        List<Size> listSizes = product.getSizes();
        for(String nameSize : request.getNameSize())
        {
            Size size = sizeRepository.findById(nameSize)
                    .orElseGet(() -> {
                        Size newSize = new Size()
                                .builder()
                                .nameSize(nameSize)
                                .build();
                        return sizeRepository.save(newSize);
                    });
            if(!listSizes.contains(size))
            {
                listSizes.add(size);
                product.setSizes(listSizes);
            }
        }
        productRepository.save(product);

        return new ApiResponse()
                .builder()
                .results(product)
                .build();
    }


    @Override
    public PageableResponse getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        Page pageOrigin = productRepository.findAll(pageable);

        // set dl vao dto response
        PageableResponse pageableResponse = new PageableResponse().builder()
                .results(pageOrigin.getContent())
                .size(pageOrigin.getSize())
                .totalElements(pageOrigin.getTotalElements())
                .totalPages(pageOrigin.getTotalPages())
                .number(pageOrigin.getNumber())
                .build();

        return pageableResponse;
    }

    @Override
    public ApiResponse getProductById(String idProduct) {
        Product product = productRepository.findById(idProduct).orElseThrow(() -> new AppException());
        return new ApiResponse().builder().results(product).build();
    }

    @Override
    public ApiResponse updateProductById(String idProduct, UpdateProductRequest request) {
        Product product = productRepository.findById(idProduct).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        product.setDescription(request.getDescription());
//        product.setManufacturer(request.getManufacturer());
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

    @Override
    public ApiResponse searchProducts() {
        List<Product> listProducts = productRepository.searchProducts(0,200,"KKVIN KEN","75f59320-7f3f-4b5c-ab21-6c85a5397c76");
        return new ApiResponse()
                .builder()
                .results(listProducts)
                .build();
    }
}