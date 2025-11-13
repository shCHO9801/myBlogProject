package com.shcho.shBlog.user.dto;

import com.shcho.shBlog.user.entity.User;

public record UserSignInResponseDto(
        String accessToken,
        String username,
        String nickname,
        String email
) {
    public static UserSignInResponseDto from(String token, User user) {
        return new UserSignInResponseDto(
                token,
                user.getUsername(),
                user.getNickname(),
                user.getEmail());
    }
}
