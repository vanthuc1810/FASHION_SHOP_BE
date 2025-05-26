package com.example.FashionShop.Controller;

import com.example.FashionShop.Dto.request.ActiveCategoryRequest;
import com.example.FashionShop.Dto.request.DeleteCategoryRequest;
import com.example.FashionShop.IServices.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.example.FashionShop.Dto.request.CategoryCreationRequest;
import com.example.FashionShop.Dto.request.CategoryUpdateRequest;
import com.example.FashionShop.Dto.response.ApiResponse;
import com.example.FashionShop.Services.CategoryService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    ICategoryService categoryService;

    @PostMapping("/create")
    public ApiResponse createCategory(@RequestBody @Valid CategoryCreationRequest request) {
        return categoryService.createCategory(request);
    }

    @PutMapping("/delete")
    public ApiResponse deleteCategory(@RequestBody DeleteCategoryRequest request) {
        return categoryService.deleteCategoryById(request);
    }

    @PutMapping("/active")
    public ApiResponse activeCategory(@RequestBody ActiveCategoryRequest request) {
        return categoryService.activeCategory(request);
    }

    @GetMapping()
    public ApiResponse getALlCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping("/{idCategory}")
    public ApiResponse getCategoryById(@PathVariable("idCategory") Integer idCategory) {
        return categoryService.getCategoryById(idCategory);
    }

    @PutMapping("/update/{idCategory}")
    public ApiResponse updateCategoryById(
            @RequestBody @Valid CategoryUpdateRequest request, @PathVariable("idCategory") Integer idCategory) {
        return categoryService.updateCategoryById(idCategory, request);
    }
}
