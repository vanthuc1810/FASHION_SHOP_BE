package com.example.FashionShop.Services;

import java.io.IOException;
import java.text.Normalizer;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.FashionShop.Configuration.PageUtil;
import com.example.FashionShop.Dto.response.ProductResponse;
import com.example.FashionShop.Dto.response.RecommentResponse;
import com.example.FashionShop.Entity.*;
import com.example.FashionShop.Repository.*;
import com.example.FashionShop.Specification.ProductSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.request.*;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Dto.response.PageableResponse;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.IServices.IProductService;
import com.example.FashionShop.Mapper.ProductMapper;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Builder
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService{
    ProductRepository productRepository;
    UserRepository userRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    ColorRepository colorRepository;
    SizeRepository sizeRepository;
    Cloudinary cloudinary;
    RestTemplate restTemplate;
    SalesOrderRepository salesOrderRepository;
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

        // Xử lý màu sắc (chuyển tất cả màu thành chữ hoa và bỏ khoảng trắng thừa)
        Set<String> uniqueColors = new HashSet<>();  // Sử dụng Set để loại bỏ trùng lặp
        for (String nameColor : request.getColors()) {
            // Bỏ khoảng trắng thừa giữa các từ, chuyển thành chữ hoa
            String colorName = nameColor.trim()                           // Xóa khoảng trắng đầu/cuối
                    .replaceAll("\\s+", " ")        // Thay thế các khoảng trắng dư thừa bằng 1 khoảng trắng
                    .toUpperCase();                 // Chuyển thành chữ hoa

            // Chỉ thêm vào nếu chưa có
            if (uniqueColors.add(colorName)) {
                Color color = colorRepository.findById(colorName).orElseGet(() -> {
                    // Nếu không tìm thấy màu sắc, tạo mới
                    Color newColor = new Color();
                    newColor.setNameColor(colorName);  // Lưu màu đã chuyển thành chữ hoa
                    return colorRepository.save(newColor);  // Lưu màu sắc mới vào database
                });
                ColorProduct colorProduct = new ColorProduct()
                        .builder()
                        .product(product)
                        .color(color)
                        .build();
                colorProducts.add(colorProduct);
            }
        }

        // Xử lý kích thước (có thể không cần thay đổi)
        Set<String> uniqueSizes = new HashSet<>();  // Sử dụng Set để loại bỏ trùng lặp
        for (String nameSize : request.getSizes()) {
            // Bỏ khoảng trắng thừa giữa các từ, chuyển thành chữ hoa
            String sizeName = nameSize.trim()                           // Xóa khoảng trắng đầu/cuối
                    .replaceAll("\\s+", " ")        // Thay thế các khoảng trắng dư thừa bằng 1 khoảng trắng
                    .toUpperCase();                 // Chuyển thành chữ hoa

            // Chỉ thêm vào nếu chưa có
            if (uniqueSizes.add(sizeName)) {
                Size size = sizeRepository.findById(sizeName).orElseGet(() -> {
                    // Nếu không tìm thấy kích thước, tạo mới
                    Size newSize = new Size();
                    newSize.setNameSize(sizeName);  // Lưu kích thước đã chuyển thành chữ hoa
                    return sizeRepository.save(newSize);  // Lưu kích thước mới vào database
                });
                SizeProduct sizeProduct = new SizeProduct()
                        .builder()
                        .product(product)
                        .size(size)
                        .build();
                sizeProducts.add(sizeProduct);
            }
        }

        product.setSizeProducts(sizeProducts);
        product.setColorProducts(colorProducts);
        productRepository.save(product);

        return new ApiResponse()
                .builder()
                .results(product)
                .message("Thêm sản phẩm thành công")
                .build();
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
        // 1. Tìm sản phẩm
        Product product = productRepository
                .findById(request.getIdProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));

        // 2. Lấy danh sách màu hiện tại (tránh set list mới!)
        List<ColorProduct> existingColorProducts = product.getColorProducts();
        List<String> incomingColors = request.getColors().stream().map(color -> {
            String newColor = color.toUpperCase();
            return newColor;
        }).toList();

        // 3. Xoá những màu không còn trong request (nếu muốn đồng bộ)
        existingColorProducts.removeIf(cp -> !incomingColors.contains(cp.getColor().getNameColor()));

        // 4. Thêm màu mới nếu chưa có trong request
        for (String nameColor : incomingColors) {
            // Kiểm tra nếu màu sắc đã có trong danh sách hiện tại
            boolean alreadyExists = existingColorProducts.stream()
                    .anyMatch(cp -> cp.getColor().getNameColor().equals(nameColor));

            if (!alreadyExists) {
                // Tìm hoặc tạo mới màu nếu không tồn tại trong database
                Color color = colorRepository.findById(nameColor).orElseGet(() -> {
                    Color newColor = new Color();
                    newColor.setNameColor(nameColor);
                    return colorRepository.save(newColor); // Lưu màu sắc mới vào database
                });

                // Tạo mới ColorProduct và thêm vào danh sách
                ColorProduct colorProduct = ColorProduct.builder()
                        .product(product)
                        .color(color)
                        .build();
                existingColorProducts.add(colorProduct);
            }
        }

        // 5. Cập nhật lại danh sách màu sắc trong sản phẩm
        product.setColorProducts(existingColorProducts);

        // 6. Lưu sản phẩm vào database
        productRepository.save(product);

        // 7. Trả về kết quả
        return ApiResponse.builder().results(product).build();
    }


    @Override
    @Transactional
    public ApiResponse addSizeToProduct(SizeCreationRequest request) {
        // 1. Tìm sản phẩm
        Product product = productRepository
                .findById(request.getIdProduct())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));

        // 2. Lấy danh sách size hiện tại (tránh set list mới!)
        List<SizeProduct> existingSizeProducts = product.getSizeProducts();
        List<String> incomingSizes = request.getSizes().stream().map(size -> {
            String newSize = size.toUpperCase();
            return newSize;
        }).toList();

        // 3. Xoá những size không còn trong request (nếu muốn đồng bộ)
        existingSizeProducts.removeIf(sp -> !incomingSizes.contains(sp.getSize().getNameSize()));

        // 4. Thêm những size mới chưa có trong request
        for (String nameSize : incomingSizes) {
            // Kiểm tra nếu size đã có trong danh sách hiện tại
            boolean alreadyExists = existingSizeProducts.stream()
                    .anyMatch(sp -> sp.getSize().getNameSize().equals(nameSize));

            if (!alreadyExists) {
                // Tìm hoặc tạo mới size nếu không tồn tại trong database
                Size size = sizeRepository.findById(nameSize).orElseGet(() -> {
                    Size newSize = new Size();
                    newSize.setNameSize(nameSize);
                    return sizeRepository.save(newSize); // Lưu size mới vào database
                });

                // Tạo mới SizeProduct và thêm vào danh sách
                SizeProduct sizeProduct = SizeProduct.builder()
                        .product(product)
                        .size(size)
                        .build();
                existingSizeProducts.add(sizeProduct);
            }
        }

        // 5. Lưu sản phẩm với các size đã được cập nhật
        product.setSizeProducts(existingSizeProducts);
        productRepository.save(product);

        // 6. Trả về kết quả
        return ApiResponse.builder().results(product).build();
    }



    @Override
    public PageableResponse getAllProducts(Pageable pageable) {
        Specification<Product> spec = Specification.where(ProductSpecification.hasDeleted(List.of(false)));
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
    public static boolean isBase64Image(String image) {
        return image != null && image.startsWith("data:image/");
    }
    @Override
    @Transactional
    public ApiResponse updateProductById(UpdateProductRequest request) throws IOException {
        Integer idProduct = request.getIdProduct();

        // 1. Tìm sản phẩm theo ID
        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOTFOUND));

        // Tìm category theo ID
        Integer idCategory = request.getIdCategory();
        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));

        product.setCategory(category);
        // 2. Cập nhật thông tin khác (trừ ảnh)
        product = productMapper.toUpdateProduct(product, request);
        if(isBase64Image(request.getImages()))
        {
            Map<String, Object> cloudResponse = uploadCloudinary(request.getImages());

            String newImages = (String) cloudResponse.get("secure_url");

            product.setImages(newImages);
        }

        // Add size and color to product
        List<String> colors = request.getColors();
        List<String> sizes = request.getSizes();
        SizeCreationRequest createSize = new SizeCreationRequest();
        createSize.setSizes(sizes);
        createSize.setIdProduct(request.getIdProduct());
        ColorCreationRequest createColor = new ColorCreationRequest();
        createColor.setColors(colors);
        createColor.setIdProduct(request.getIdProduct());
        addSizeToProduct(createSize);
        addColorToProduct(createColor);

        // 4. Lưu lại sản phẩm
        productRepository.save(product);

        // 5. Trả về phản hồi
        return ApiResponse.builder().results(product).build();
    }

    public Map<String, Object> uploadCloudinary(String base64Image) throws IOException {
        return cloudinary.uploader()
                .upload(base64Image.getBytes(), ObjectUtils.emptyMap());
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
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse activeProduct(ActiveProductRequest request) {
        List<Integer> idProducts = request.getIdProducts();
        List<Product> products = productRepository.findAllById(idProducts);

        if (products.size() != idProducts.size()) {
            throw new AppException(ErrorCode.PRODUCT_NOTFOUND);
        }
        Set<Integer> idCategoryList = new HashSet<>();
        products.forEach(product -> {
            idCategoryList.add(product.getCategory().getIdCategory());
            product.setDeleted(false);
        });
        List<Category> categoryList = categoryRepository.findAllById(idCategoryList);
        categoryList.forEach(category -> {
            category.setDeleted(false);
        });
        categoryRepository.saveAll(categoryList);
        productRepository.saveAll(products);
        return ApiResponse
                .builder()
                .code(200)
                .message("Kích hoạt sản phẩm thành công!!!")
                .build();
    }

    @Override
    public PageableResponse filterProducts(String query, FilterProductRequest request, Pageable pageable) {
        double minPrice = request.getPrices().get(0);
        double maxPrice = request.getPrices().get(1);
        List<String> manufacturers = request.getManufacturers();
        List<Integer> idCategorys = request.getIdCategorys();
        List<String> colors = request.getColors();
        List<String> sizes = request.getSizes();
        List<Boolean> isDeleted = request.getIsDeleted();
        // filter Manufacturer - Price - Colors - Size - Category
        Specification<Product> spec = Specification
                .where(ProductSpecification.hasManufacturer(manufacturers))
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
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("idProduct").descending()
        );
        Page<Product> pageOrigin = productRepository.findAll(spec, sortedPageable);

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
    public PageableResponse getRecommentProduct(FilterProductRequest request, Pageable pageable) {
        String url = "http://127.0.0.1:5000/recommend";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer idUser = Integer.valueOf(authentication.getName());
        SalesOrder salesOrder = salesOrderRepository.findLatestByUserId(idUser);
        RecommentRequest recommentRequest = new RecommentRequest();
        Card card = salesOrder.getCard();
        List<Integer> idProducts = new ArrayList<>();
        for (CardItem cardItem : card.getCardItems())
        {
            idProducts.add(cardItem.getProduct().getIdProduct());
        }
        recommentRequest.setIdProducts(idProducts);
        recommentRequest.setIdSalesOrder(salesOrder.getIdSalesOrder());

        ResponseEntity<RecommentResponse> response = restTemplate.postForEntity(url, recommentRequest, RecommentResponse.class);
        RecommentResponse recommentResponse = response.getBody();
        List<Integer> idProductsResponse = recommentResponse.getSuggested_products();

        List<Product> productList = productRepository.findAllById(idProductsResponse);

        Map<Integer, Product> productMap = productList.stream()
                .collect(Collectors.toMap(Product::getIdProduct, Function.identity()));

        List<Product> productList1 = idProductsResponse.stream()
                .map(productMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<Product> productList2 = new ArrayList<>();
        productList1.stream()
                .filter(product -> !product.isDeleted())  // Lọc các sản phẩm không bị xóa
                .forEach(productList2::add);  // Thêm vào productList2
        Page<Product> page = PageUtil.toPage(productList2, pageable);

        List<ProductResponse> productResponseList = page.getContent().stream().map(productMapper::toProductResponse).toList();
        return PageableResponse
                .builder()
                .results(productResponseList)
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .number(page.getNumber())
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
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("idProduct").descending()
        );

        Page<Product> listProducts = productRepository.findAll(spec, sortedPageable);
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
