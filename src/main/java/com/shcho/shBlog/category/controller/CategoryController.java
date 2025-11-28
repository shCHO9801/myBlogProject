package com.shcho.shBlog.category.controller;

import com.shcho.shBlog.category.dto.CategoryResponseDto;
import com.shcho.shBlog.category.dto.CreateCategoryRequestDto;
import com.shcho.shBlog.category.dto.CreateCategoryResponseDto;
import com.shcho.shBlog.category.entity.Category;
import com.shcho.shBlog.category.service.CategoryService;
import com.shcho.shBlog.user.auth.CustomUserDetails;
import com.shcho.shBlog.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CreateCategoryResponseDto> createCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateCategoryRequestDto requestDto
    ) {
        Long userId = userDetails.getUserId();
        Category newCategory = categoryService.createCategory(userId, requestDto);

        return ResponseEntity.ok(CreateCategoryResponseDto.from(newCategory));
    }

    @GetMapping("/{blogPageId}")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategoriesByBlogPage(
            @PathVariable Long blogPageId
    ) {
        List<CategoryResponseDto> categories = categoryService.getAllCategoriesByBlogPageId(blogPageId)
                .stream()
                .map(CategoryResponseDto::from)
                .toList();

        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long categoryId,
            @Valid @RequestBody CreateCategoryRequestDto requestDto
    ) {
        Long userId = userDetails.getUserId();
        Category updatedCategory =
                categoryService.updateCategory(userId, categoryId, requestDto);

        return ResponseEntity.ok(CategoryResponseDto.from(updatedCategory));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long categoryId
    ) {
        Long userId = userDetails.getUserId();
        categoryService.deleteCategoryByCategoryId(userId, categoryId);

        return ResponseEntity.noContent().build();
    }
}
