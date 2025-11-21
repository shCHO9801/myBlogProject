package com.shcho.shBlog.blogpage.dto;

import com.shcho.shBlog.blogpage.entity.BlogPage;

public record BlogPageResponseDto(
        Long id,
        String title,
        String intro,
        String bannerImageUrl,
        String userNickName,
        String userProfileImageUrl
) {
    public static BlogPageResponseDto from(BlogPage blogPage) {
        return new BlogPageResponseDto(
                blogPage.getId(),
                blogPage.getTitle(),
                blogPage.getIntro(),
                blogPage.getBannerImageUrl(),
                blogPage.getUser().getNickname(),
                blogPage.getUser().getProfileImageUrl()
        );
    }
}
