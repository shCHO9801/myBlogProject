package com.shcho.shBlog.blogpage.dto;

import jakarta.validation.constraints.NotBlank;

public record BlogPageUpdateRequestDto(
        @NotBlank String title,
        String intro,
        String bannerImageUrl
) {
}
