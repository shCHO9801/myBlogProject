package com.shcho.shBlog.user.service;

import com.shcho.shBlog.libs.exception.CustomException;
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

        return userRepository.save(signUpUser);
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
