package com.shcho.shBlog.category.dto;

import com.shcho.shBlog.category.entity.Category;

public record CreateCategoryResponseDto(
        Long blogPageId,
        Long categoryId,
        String categoryName,
        String categoryDescription
) {
    public static CreateCategoryResponseDto from(Category category) {
        return new CreateCategoryResponseDto(
                category.getBlogPage().getId(),
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
