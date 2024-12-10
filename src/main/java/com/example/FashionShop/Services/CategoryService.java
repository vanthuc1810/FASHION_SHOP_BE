package com.example.FashionShop.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.FashionShop.Dto.request.CategoryCreationRequest;
import com.example.FashionShop.Dto.request.CategoryUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.Category;
import com.example.FashionShop.Enum.ErrorCode;
import com.example.FashionShop.Exception.AppException;
import com.example.FashionShop.Repository.CategoryRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
    CategoryRepository categoryRepository;

    public ApiResponse createCategory(CategoryCreationRequest request) {
        Category category = new Category().builder().name(request.getName()).build();
        categoryRepository.save(category);
        return new ApiResponse().builder().results(category).build();
    }

    public ApiResponse<List<Category>> getAllCategory() {
        List<Category> listCategory = categoryRepository.findAll();
        return ApiResponse.<List<Category>>builder().results(listCategory).build();
    }

    public ApiResponse<Category> getCategoryById(Integer idCategory) {
        Category category = categoryRepository
                .findById(idCategory)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));
        return ApiResponse.<Category>builder().results(category).build();
    }

    public ApiResponse deleteCategoryById(Integer idCategory) {
        categoryRepository.deleteById(idCategory);
        return new ApiResponse().builder().message("Xóa danh mục thành công").build();
    }

    public ApiResponse updateCategoryById(Integer idCategory, CategoryUpdateRequest request) {
        Category category = categoryRepository
                .findById(idCategory)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));
        category.setName(request.getName());
        categoryRepository.save(category);
        return new ApiResponse().builder().results(category).build();
    }
}
