package com.example.FashionShop.IServices;

import com.example.FashionShop.Dto.request.ActiveCategoryRequest;
import com.example.FashionShop.Dto.request.CategoryCreationRequest;
import com.example.FashionShop.Dto.request.CategoryUpdateRequest;
import com.example.FashionShop.Dto.request.DeleteCategoryRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Entity.Category;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ICategoryService {
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse createCategory(CategoryCreationRequest request);

    ApiResponse<List<Category>> getAllCategory();

    ApiResponse<Category> getCategoryById(Integer idCategory);

    ApiResponse deleteCategoryById(DeleteCategoryRequest request);

    ApiResponse activeCategory(ActiveCategoryRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse updateCategoryById(Integer idCategory, CategoryUpdateRequest request);
}
