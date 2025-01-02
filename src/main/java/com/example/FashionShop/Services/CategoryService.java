package com.example.FashionShop.Services;

import java.util.List;
import java.util.Optional;

import com.example.FashionShop.IServices.ICategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class CategoryService implements ICategoryService {
    CategoryRepository categoryRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse createCategory(CategoryCreationRequest request) {
        Optional<Category> existingCategory = categoryRepository.findByName(request.getName());
        Category category = new Category();
        if(!existingCategory.isPresent())
        {
            category = Category
                    .builder()
                    .name(request.getName())
                    .build();
            categoryRepository.save(category);
        }else
        {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        return new ApiResponse().builder().results(category).build();
    }
    @Override
    public ApiResponse<List<Category>> getAllCategory() {
        List<Category> listCategory = categoryRepository.findAll();
        return ApiResponse.<List<Category>>builder().results(listCategory).build();
    }
    @Override
    public ApiResponse<Category> getCategoryById(Integer idCategory) {
        Category category = categoryRepository
                .findById(idCategory)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));
        return ApiResponse.<Category>builder().results(category).build();
    }
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse deleteCategoryById(Integer idCategory) {
        categoryRepository.deleteById(idCategory);
        return new ApiResponse().builder().message("Xóa danh mục thành công").build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse updateCategoryById(Integer idCategory, CategoryUpdateRequest request) {
        Category category = categoryRepository
                .findById(idCategory)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOTFOUND));
        category.setName(request.getName());
        categoryRepository.save(category);
        return new ApiResponse().builder().results(category).build();
    }
}
