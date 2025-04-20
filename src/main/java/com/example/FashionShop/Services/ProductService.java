package com.example.FashionShop.Services;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.example.FashionShop.Dto.response.ProductResponse;
import com.example.FashionShop.Entity.*;
import com.example.FashionShop.Specification.ProductSpecification;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.transaction.annotation.Transactional;

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
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse createProduct(ProductCreationRequest request) {
        Category category = categoryRepository
                .findById(request.getIdCategory())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));

        Product product = productMapper.toProduct(request, category);
        String seri = "FAS_" + getInitials(category.getName());
        product.setSeriProduct(seri);
        Product myProduct = productRepository.save(product);

        seri += String.format("%05d", myProduct.getIdProduct());
        myProduct.setSeriProduct(seri);
        productRepository.save(myProduct);

        List<ColorProduct> colorProducts = new ArrayList<>();
        List<SizeProduct> sizeProducts = new ArrayList<>();

        for (String nameColor : request.getColors())
        {
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
            colorProducts.add(colorProduct);
        }

        for (String nameSize : request.getSizes())
        {
            Size size = sizeRepository.findById(nameSize).orElseGet(() -> {
                // Nếu không tìm thấy màu sắc, tạo mới
                Size newSize = new Size();
                newSize.setNameSize(nameSize);
                return sizeRepository.save(newSize); // Lưu màu sắc mới vào database
            });
            SizeProduct sizeProduct = new SizeProduct()
                    .builder()
                    .product(product)
                    .size(size)
                    .build();
            sizeProducts.add(sizeProduct);
        }

        product.setSizeProducts(sizeProducts);
        product.setColorProducts(colorProducts);
        productRepository.save(product);
        return new ApiResponse().builder().results(product).build();
    }
    public static String removeVietnameseTones(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        normalized = pattern.matcher(normalized).replaceAll("");
        return normalized.replaceAll("đ", "d").replaceAll("Đ", "D");
    }

    // Lấy ký tự đầu tiên mỗi từ sau khi đã bỏ dấu
    public static String getInitials(String input) {
        if (input == null || input.isEmpty()) return "";

        // Bỏ dấu trước
        input = removeVietnameseTones(input);

        String[] words = input.trim().split("\\s+");
        StringBuilder initials = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }

        return initials.toString().toUpperCase();
    }
    @Override
    @Transactional
    public ApiResponse addColorToProduct(ColorCreationRequest request) {
        Product product = productRepository
                .findById(request.getIdProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        // 2. Tìm hoặc tạo mới màu sắc theo nameColor
        List<ColorProduct> productColors = new ArrayList<>(); // Lấy danh sách màu của sản phẩm

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
            productColors.add(colorProduct);
        }
        product.setColorProducts(productColors); // Cập nhật danh sách màu cho sản phẩm

        // 3. Luu san pham
        productRepository.save(product);

        return new ApiResponse().builder().results(product).build();
    }

    @Override
    @Transactional
    public ApiResponse addSizeToProduct(SizeCreationRequest request) {
        Product product = productRepository
                .findById(request.getIdProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));
        // 2. Tìm hoặc tạo mới màu sắc theo nameColor
        List<SizeProduct> sizeProducts = new ArrayList<>(); // Lấy danh sách màu của sản phẩm

        for (String nameSize : request.getSizes()) {
            Size size = sizeRepository.findById(nameSize).orElseGet(() -> {
                // Nếu không tìm thấy màu sắc, tạo mới
                Size newSize = new Size();
                newSize.setNameSize(nameSize);
                return sizeRepository.save(newSize); // Lưu màu sắc mới vào database
            });
            SizeProduct sizeProduct = new SizeProduct()
                    .builder()
                    .product(product)
                    .size(size)
                    .build();
            sizeProducts.add(sizeProduct);
        }
        product.setSizeProducts(sizeProducts); // Cập nhật danh sách màu cho sản phẩm

        // 3. Luu san pham
        productRepository.save(product);

        return new ApiResponse().builder().results(product).build();
    }

    @Override
    public PageableResponse getAllProducts(Pageable pageable) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasDeleted(false));
        Page<Product> pageOrigin = productRepository.findAll(spec, pageable);
        List<Product> listProduct = pageOrigin.getContent();
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (Product product : listProduct)
        {
            ProductResponse productResponse = productMapper.toProductResponse(product);
            productResponseList.add(productResponse);
        }
        // set dl vao dto response
        PageableResponse pageableResponse = new PageableResponse()
                .builder()
                .results(productResponseList)
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
        if(product.isDeleted()){
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        }
        ProductResponse productResponse = productMapper.toProductResponse(product);
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
        product = productMapper.toUpdateProduct(product, request);
        productRepository.save(product);
        return new ApiResponse().builder().results(product).build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deleteProduct(DeleteProductRequest request) {
        List<Integer> idProducts = request.getIdProducts();

        List<Product> products = productRepository.findAllById(idProducts);

        if (products.size() != idProducts.size()) {
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        }

        products.forEach(product -> product.setDeleted(true));
        productRepository.saveAll(products);
        return ApiResponse
                .builder()
                .code(200)
                .message("Xóa sản pẩm thành công!!!")
                .build();
    }

    @Override
    public PageableResponse filterProducts(String query, FilterProductRequest request, Pageable pageable) {
        Long minPrice = request.getPrices().get(0);
        Long maxPrice = request.getPrices().get(1);
        List<String> manufacturers = request.getManufacturers();
        List<Integer> idCategorys = request.getIdCategorys();
        List<String> colors = request.getColors();
        List<String> sizes = request.getSizes();
        boolean isDeleted = request.isDeleted();
        // filter Manufacturer - Price - Colors - Size - Category
        Specification<Product> spec = Specification.where(ProductSpecification.hasManufacturer(manufacturers))
                .and(ProductSpecification.hasColors(colors))
                .and(ProductSpecification.hasSizes(sizes))
                .and(ProductSpecification.hasPriceInRange(minPrice,maxPrice))
                .and(ProductSpecification.hasIdCategory(idCategorys))
                .and(ProductSpecification.hasDeleted(isDeleted));

        if (query != null && !query.trim().isEmpty()) {
            String[] words = query.trim().split("\\s+");
            for (String word : words) {
                spec = spec.and(ProductSpecification.nameContains(word));
            }
        }
        Page<Product> pageOrigin = productRepository.findAll(spec,pageable);
        List<Product> productList = pageOrigin.getContent();
        List<ProductResponse> productResponseList = new ArrayList<>();
        for(Product product : productList)
        {
            ProductResponse productResponse = productMapper.toProductResponse(product);
            productResponseList.add(productResponse);
        }
        return PageableResponse
                .builder()
                .results(productResponseList)
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
            String[] words = keyword.trim().split("\\s+");
            for (String word : words) {
                spec = spec.and(ProductSpecification.nameContains(word));
            }
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

    @Override
    public ApiResponse getManufracture() {
        List<String> manufractureList = productRepository.getAllManufacturer();
        return ApiResponse
                .builder()
                .code(200)
                .results(manufractureList)
                .build();
    }
}
