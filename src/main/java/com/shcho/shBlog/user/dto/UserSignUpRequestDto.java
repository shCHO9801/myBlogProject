package com.shcho.shBlog.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserSignUpRequestDto(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String nickname,
        @NotBlank String email
) {
}
