package com.shcho.shBlog.user.controller;

import com.shcho.shBlog.user.dto.UserSignUpRequestDto;
import com.shcho.shBlog.user.dto.UserSignUpResponseDto;
import com.shcho.shBlog.user.entity.User;
import com.shcho.shBlog.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignUpResponseDto> signUp(
            @Valid @RequestBody UserSignUpRequestDto requestDto) {
        User signUpUser = userService.signUpUser(requestDto);

        return ResponseEntity.ok(UserSignUpResponseDto.from(signUpUser));
    }
}
