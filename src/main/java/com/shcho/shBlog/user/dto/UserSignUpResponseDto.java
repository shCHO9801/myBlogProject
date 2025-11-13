package com.shcho.shBlog.user.dto;

import com.shcho.shBlog.user.entity.User;

public record UserSignUpResponseDto(
        Long userId,
        String username,
        String nickname,
        String email
) {
    public static UserSignUpResponseDto from(User user) {
        return new UserSignUpResponseDto(
                user.getUserId(),
                user.getUsername(),
                user.getNickname(),
                user.getEmail()
        );
    }
}
