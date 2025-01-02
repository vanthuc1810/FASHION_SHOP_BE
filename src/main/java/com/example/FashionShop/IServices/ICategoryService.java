package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.CategoryCreationRequest;
import com.example.FashionShop.Dto.request.CategoryUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.Category;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ICategoryService {
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse createCategory(CategoryCreationRequest request);

    ApiResponse<List<Category>> getAllCategory();

    ApiResponse<Category> getCategoryById(Integer idCategory);

    ApiResponse deleteCategoryById(Integer idCategory);

    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse updateCategoryById(Integer idCategory, CategoryUpdateRequest request);
}
