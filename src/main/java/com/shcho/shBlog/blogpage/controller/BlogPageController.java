package com.shcho.shBlog.blogpage.controller;

import com.shcho.shBlog.blogpage.dto.BlogPageResponseDto;
import com.shcho.shBlog.blogpage.entity.BlogPage;
import com.shcho.shBlog.blogpage.service.BlogPageService;
import com.shcho.shBlog.user.auth.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/blog/page")
@RequiredArgsConstructor
public class BlogPageController {

    private final BlogPageService blogPageService;

    @GetMapping("/me")
    public ResponseEntity<BlogPageResponseDto> getBlogPage(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BlogPage blogPage = blogPageService.getBlogPageById(userDetails.getUserId());

        return ResponseEntity.ok(BlogPageResponseDto.from(blogPage));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BlogPageResponseDto> getBlogPageByUserId(
            @PathVariable Long userId
    ) {
        BlogPage blogPage = blogPageService.getBlogPageById(userId);

        return ResponseEntity.ok(BlogPageResponseDto.from(blogPage));
    }
}
