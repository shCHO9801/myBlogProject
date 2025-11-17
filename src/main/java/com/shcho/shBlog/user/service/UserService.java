package com.shcho.shBlog.user.service;

import com.shcho.shBlog.blogpage.entity.BlogPage;
import com.shcho.shBlog.common.util.JwtProvider;
import com.shcho.shBlog.libs.exception.CustomException;
import com.shcho.shBlog.user.dto.UserSignInRequestDto;
import com.shcho.shBlog.user.dto.UserSignUpRequestDto;
import com.shcho.shBlog.user.entity.User;
import com.shcho.shBlog.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.shcho.shBlog.libs.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public User signUpUser(UserSignUpRequestDto dto) {
        if (existsByUsername(dto.username())) {
            throw new CustomException(DUPLICATED_USERNAME);
        }

        if (existsByNickname(dto.nickname())) {
            throw new CustomException(DUPLICATED_NICKNAME);
        }

        if (existsByEmail(dto.email())) {
            throw new CustomException(DUPLICATED_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(dto.password());

        User signUpUser = User.of(
                dto.username(),
                encodedPassword,
                dto.nickname(),
                dto.email()
        );

        signUpUser.setBlogPage(BlogPage.createDefault(signUpUser));

        return userRepository.save(signUpUser);
    }

    public User signInUser(
            UserSignInRequestDto requestDto
    ) {
        String username = requestDto.username();
        String rawPassword = requestDto.password();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(INVALID_USERNAME_OR_PASSWORD));

        if (user.isDeleted()) {
            throw new CustomException(USER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new CustomException(INVALID_USERNAME_OR_PASSWORD);
        }

        return user;
    }

    public String getUserToken(User user) {
        return jwtProvider.createToken(user.getUsername(), user.getRole());
    }

    private boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    private boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
