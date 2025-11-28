package com.shcho.shBlog.category.dto;

import jakarta.validation.Valid;

public record UpdateCategoryRequestDto(
        @Valid String name,
        String description
) {
}
