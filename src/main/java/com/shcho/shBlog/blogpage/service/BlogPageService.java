package com.shcho.shBlog.blogpage.service;

import com.shcho.shBlog.blogpage.entity.BlogPage;
import com.shcho.shBlog.blogpage.repository.BlogPageRepository;
import com.shcho.shBlog.libs.exception.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.shcho.shBlog.libs.exception.ErrorCode.BLOG_PAGE_NOT_FOUND;

@Service
@AllArgsConstructor
public class BlogPageService {

    private final BlogPageRepository blogPageRepository;

    public BlogPage getBlogPageById(Long userId) {
        return blogPageRepository.getBlogPageByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(BLOG_PAGE_NOT_FOUND));
    }

}
