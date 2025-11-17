package com.shcho.shBlog.blogpage.entity;

import com.shcho.shBlog.common.entity.BaseEntity;
import com.shcho.shBlog.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogPage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String intro;

    private String bannerImageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static BlogPage createDefault(User user){
        return BlogPage.builder()
                .title(user.getNickname() + "의 블로그")
                .intro("")
                .bannerImageUrl(null)
                .user(user)
                .build();
    }
}
