package com.example.FashionShop.Controller;

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
    CategoryService categoryService;

    @PostMapping("/create")
    public ApiResponse createCategory(@RequestBody CategoryCreationRequest request) {
        return categoryService.createCategory(request);
    }

    @DeleteMapping("/delete/{idCategory}")
    public ApiResponse deleteCategory(@PathVariable("idCategory") Integer idCategory) {
        return categoryService.deleteCategoryById(idCategory);
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
            @RequestBody CategoryUpdateRequest request, @PathVariable("idCategory") Integer idCategory) {
        return categoryService.updateCategoryById(idCategory, request);
    }
}
