package com.shcho.shBlog.user.entity;

import com.shcho.shBlog.common.entity.BaseEntity;
import com.shcho.shBlog.libs.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.shcho.shBlog.libs.exception.ErrorCode.ALREADY_DELETED_USER;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column
    private String password;

    @Column
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private Role role;

    @Column
    private LocalDateTime deletedAt;

    public static User of(String username, String encodedPassword, String nickname, String email) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .nickname(nickname)
                .email(email)
                .role(Role.USER)
                .build();
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void deleteProfileImageUrl() {
        this.profileImageUrl = null;
    }

    public void withdraw() {
        if (this.deletedAt != null) {
            throw new CustomException(ALREADY_DELETED_USER);
        }
        this.deletedAt = LocalDateTime.now();
        this.username = "DELETED_" + this.username;
        this.password = null;
        this.nickname = null;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
