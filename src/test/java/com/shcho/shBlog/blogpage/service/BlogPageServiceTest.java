package com.shcho.shBlog.blogpage.service;

import com.shcho.shBlog.blogpage.dto.BlogPageUpdateRequestDto;
import com.shcho.shBlog.blogpage.entity.BlogPage;
import com.shcho.shBlog.blogpage.repository.BlogPageRepository;
import com.shcho.shBlog.libs.exception.CustomException;
import com.shcho.shBlog.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.shcho.shBlog.libs.exception.ErrorCode.BLOG_PAGE_NOT_FOUND;
import static com.shcho.shBlog.libs.exception.ErrorCode.NO_PERMISSION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Blog Page Service Unit Test")
class BlogPageServiceTest {

    @Mock
    private BlogPageRepository blogPageRepository;

    @InjectMocks
    private BlogPageService blogPageService;

    public BlogPageServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Blog Page 조회 성공")
    void getBlogPageSuccess() {
        // given
        User user = User.builder()
                .userId(1L)
                .nickname("nickname")
                .build();

        BlogPage blogPage = BlogPage.builder()
                .id(1L)
                .title("nickname의 블로그")
                .intro("")
                .bannerImageUrl(null)
                .user(user)
                .build();

        when(blogPageRepository.getBlogPageByUser_UserId(user.getUserId()))
                .thenReturn(Optional.of(blogPage));

        // when
        BlogPage foundBlogPage = blogPageService.getBlogPageById(user.getUserId());

        // then
        assertNotNull(foundBlogPage);
        assertEquals(1L, foundBlogPage.getId());
        assertEquals("nickname의 블로그", foundBlogPage.getTitle());
        assertEquals(user, foundBlogPage.getUser());
        verify(blogPageRepository, times(1))
                .getBlogPageByUser_UserId(user.getUserId());
    }

    @Test
    @DisplayName("Blog Page 조회 실패 - BLOG_PAGE_NOT_FOUND")
    void getBlogPageFailureBlogPageNotFound() {
        // given
        Long userId = 999L;

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> blogPageService.getBlogPageById(userId));

        assertEquals(BLOG_PAGE_NOT_FOUND, exception.getErrorCode());
        verify(blogPageRepository, times(1))
                .getBlogPageByUser_UserId(999L);

    }

    @Test
    @DisplayName("Blog Page 수정 성공")
    void updateBlogPageSuccess() {
        // given
        Long userId = 1L;

        User user = User.builder()
                .userId(1L)
                .nickname("nickname")
                .build();

        BlogPage blogPage = BlogPage.builder()
                .id(1L)
                .title("oldTitle")
                .intro("oldIntro")
                .bannerImageUrl(null)
                .user(user)
                .build();

        BlogPageUpdateRequestDto requestDto = new BlogPageUpdateRequestDto(
                "newTitle",
                "newIntro",
                "newBanner"
        );

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.of(blogPage));

        when(blogPageRepository.save(blogPage))
                .thenReturn(blogPage);

        // when
        BlogPage updatedBlogPage = blogPageService.updateMyBlogPage(
                userId, requestDto
        );

        // then
        assertEquals("newTitle", updatedBlogPage.getTitle());
        assertEquals("newIntro", updatedBlogPage.getIntro());
        assertEquals("newBanner", updatedBlogPage.getBannerImageUrl());

        verify(blogPageRepository, times(1)).save(blogPage);
    }

    @Test
    @DisplayName("Blog Page 수정 실패 - BLOG_PAGE_NOT_FOUND")
    void updateBlogPageFailureBlogPageNotFound() {
        // given
        Long userId = 999L;

        BlogPageUpdateRequestDto requestDto = new BlogPageUpdateRequestDto(
                "title",
                "intro",
                "banner"
        );

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.empty());

        // when && then
        CustomException exception = assertThrows(CustomException.class,
                () -> blogPageService.updateMyBlogPage(userId, requestDto)
        );

        assertEquals(BLOG_PAGE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("Blog Page 수정 실패 - 본인 블로그가 아님 - NO_PERMISSION")
    void updateBlogPageFailureNotOwner() {
        // given
        Long editorUserId = 1L;
        Long ownerUserId = 2L;

        User owner = User.builder()
                .userId(ownerUserId)
                .nickname("owner")
                .build();

        BlogPage blogPage = BlogPage.builder()
                .id(1L)
                .title("oldTitle")
                .intro("oldIntro")
                .bannerImageUrl(null)
                .user(owner)
                .build();

        BlogPageUpdateRequestDto requestDto = new BlogPageUpdateRequestDto(
                "newTitle",
                "newIntro",
                "newBanner"
        );

        when(blogPageRepository.getBlogPageByUser_UserId(editorUserId))
                .thenReturn(Optional.of(blogPage));

        // when && then
        CustomException exception = assertThrows(CustomException.class,
                () -> blogPageService.updateMyBlogPage(editorUserId, requestDto));

        assertEquals(NO_PERMISSION, exception.getErrorCode());
    }
}