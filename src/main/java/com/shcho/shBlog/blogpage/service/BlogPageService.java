package com.shcho.shBlog.blogpage.service;

import com.shcho.shBlog.blogpage.dto.BlogPageUpdateRequestDto;
import com.shcho.shBlog.blogpage.entity.BlogPage;
import com.shcho.shBlog.blogpage.repository.BlogPageRepository;
import com.shcho.shBlog.libs.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.shcho.shBlog.libs.exception.ErrorCode.BLOG_PAGE_NOT_FOUND;
import static com.shcho.shBlog.libs.exception.ErrorCode.NO_PERMISSION;

@Service
@AllArgsConstructor
public class BlogPageService {

    private final BlogPageRepository blogPageRepository;

    public BlogPage getBlogPageById(Long userId) {
        return blogPageRepository.getBlogPageByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(BLOG_PAGE_NOT_FOUND));
    }

    public BlogPage updateMyBlogPage(Long userId, BlogPageUpdateRequestDto updateRequestDto) {
        BlogPage blogPage = getBlogPageById(userId);

        if (!blogPage.getUser().getUserId().equals(userId)) {
            throw new CustomException(NO_PERMISSION);
        }

        blogPage.setTitle(updateRequestDto.title());
        blogPage.setIntro(updateRequestDto.intro());
        blogPage.setBannerImageUrl(updateRequestDto.bannerImageUrl());

        return blogPageRepository.save(blogPage);
    }

}
