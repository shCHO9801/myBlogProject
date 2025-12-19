package com.shcho.shBlog.category.service;

import com.shcho.shBlog.blogpage.entity.BlogPage;
import com.shcho.shBlog.blogpage.repository.BlogPageRepository;
import com.shcho.shBlog.category.dto.CreateCategoryRequestDto;
import com.shcho.shBlog.category.dto.UpdateCategoryRequestDto;
import com.shcho.shBlog.category.entity.Category;
import com.shcho.shBlog.category.repository.CategoryRepository;
import com.shcho.shBlog.libs.exception.CustomException;
import com.shcho.shBlog.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.shcho.shBlog.libs.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Category Service Unit Test")
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BlogPageRepository blogPageRepository;

    @InjectMocks
    private CategoryService categoryService;

    public CategoryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Category 생성 성공")
    void createCategorySuccess() {
        // given
        Long userId = 1L;
        Long blogPageId = 1L;

        User user = buildUser(userId);

        BlogPage blogPage = buildBlogPage(blogPageId, user);

        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto("newCategory", "newDescription");

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.of(blogPage));
        when(categoryRepository.existsByBlogPage_IdAndName(blogPageId, "newCategory"))
                .thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        // when
        Category category = categoryService.createCategory(userId, requestDto);

        // then
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());

        // captor 객체 검증
        assertAll(
                () -> assertEquals(requestDto.name(), categoryCaptor.getValue().getName()),
                () -> assertEquals(requestDto.description(), categoryCaptor.getValue().getDescription()),
                () -> assertEquals(blogPageId, categoryCaptor.getValue().getBlogPage().getId())
        );

        // service return 객체 검증
        assertAll(
                () -> assertEquals(requestDto.name(), category.getName()),
                () -> assertEquals(requestDto.description(), category.getDescription())
        );
    }

    @Test
    @DisplayName("Category 생성 실패 - 중복된 Category 명")
    void createCategoryFailedDuplicatedCategoryName() {
        // given
        Long userId = 1L;
        Long blogPageId = 1L;

        User user = buildUser(userId);

        BlogPage blogPage = buildBlogPage(blogPageId, user);

        CreateCategoryRequestDto requestDto =
                new CreateCategoryRequestDto("newCategory", "newDescription");

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.of(blogPage));
        when(categoryRepository.existsByBlogPage_IdAndName(blogPageId, "newCategory"))
                .thenReturn(true);
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> categoryService.createCategory(userId, requestDto));

        assertEquals(DUPLICATED_CATEGORY_NAME, exception.getErrorCode());
    }

    @Test
    @DisplayName("Category 조회 성공")
    void getAllCategoriesByBlogPageIdSuccess() {
        // given
        Long blogPageId = 1L;

        BlogPage blogPage = buildBlogPage(blogPageId);

        List<Category> categories = new ArrayList<>();

        for (long i = 1L; i <= 3L; i++) {
            categories.add(
                    Category.builder()
                            .id(i)
                            .name("Category " + i)
                            .description("No." + i + "category")
                            .build()
            );
        }

        when(blogPageRepository.findById(blogPageId))
                .thenReturn(Optional.of(blogPage));
        when(categoryRepository.findAllByBlogPage_IdOrderByNameAsc(blogPageId))
                .thenReturn(categories);

        // when
        List<Category> allCategoriesByBlogPageId =
                categoryService.getAllCategoriesByBlogPageId(blogPageId);

        // then
        assertEquals(3, allCategoriesByBlogPageId.size());
        assertAll(
                () -> assertEquals(categories.get(0), allCategoriesByBlogPageId.get(0)),
                () -> assertEquals(categories.get(1), allCategoriesByBlogPageId.get(1))
        );
    }

    @Test
    @DisplayName("Category 수정 성공")
    void updateCategorySuccess() {
        // given
        Long blogPageId = 1L;
        Long userId = 1L;
        Long categoryId = 1L;

        User user = buildUser(userId);
        BlogPage blogPage = buildBlogPage(blogPageId, user);

        Category oldCategory = Category.builder()
                .id(categoryId)
                .name("oldCategory")
                .description("oldDescription")
                .blogPage(blogPage)
                .build();

        UpdateCategoryRequestDto requestDto =
                new UpdateCategoryRequestDto("updateCategory", "updateDescription");

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.of(blogPage));
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(oldCategory));
        when(categoryRepository.existsByBlogPage_IdAndName(blogPageId, requestDto.name()))
                .thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Category updatedCategory = categoryService.updateCategory(userId, categoryId, requestDto);

        // then
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category savedCategory = categoryCaptor.getValue();

        assertAll(
                () -> assertEquals(categoryId, savedCategory.getId()),
                () -> assertEquals(requestDto.name(), savedCategory.getName()),
                () -> assertEquals(requestDto.description(), savedCategory.getDescription()),
                () -> assertEquals(blogPageId, savedCategory.getBlogPage().getId())
        );

        // service 반환값도 동일하게 검증
        assertAll(
                () -> assertEquals(categoryId, updatedCategory.getId()),
                () -> assertEquals(requestDto.name(), updatedCategory.getName()),
                () -> assertEquals(requestDto.description(), updatedCategory.getDescription())
        );
    }

    @Test
    @DisplayName("Category 수정 성공 - Description 수정")
    void updateCategorySuccessChangeDescription() {
        Long blogPageId = 1L;
        Long userId = 1L;
        Long categoryId = 1L;

        User user = buildUser(userId);
        BlogPage blogPage = buildBlogPage(blogPageId, user);

        Category oldCategory = Category.builder()
                .id(categoryId)
                .name("oldCategory")
                .description("oldDescription")
                .blogPage(blogPage)
                .build();

        UpdateCategoryRequestDto requestDto =
                new UpdateCategoryRequestDto("oldCategory", "updateDescription");

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.of(blogPage));
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(oldCategory));
        when(categoryRepository.existsByBlogPage_IdAndName(blogPageId, requestDto.name()))
                .thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Category updatedCategory = categoryService.updateCategory(userId, categoryId, requestDto);

        // then
        ArgumentCaptor<Category> categoryCaptor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(categoryCaptor.capture());
        Category savedCategory = categoryCaptor.getValue();

        assertAll(
                () -> assertEquals(categoryId, savedCategory.getId()),
                () -> assertEquals(requestDto.name(), savedCategory.getName()),
                () -> assertEquals(requestDto.description(), savedCategory.getDescription()),
                () -> assertEquals(blogPageId, savedCategory.getBlogPage().getId())
        );

        // service 반환값도 동일하게 검증
        assertAll(
                () -> assertEquals(categoryId, updatedCategory.getId()),
                () -> assertEquals(requestDto.name(), updatedCategory.getName()),
                () -> assertEquals(requestDto.description(), updatedCategory.getDescription())
        );
    }

    @Test
    @DisplayName("Category 수정 실패 - 권한 없음 (다른 유저의 카테고리)")
    void updateCategoryFailedForbidden() {
        Long blogPageId = 1L;
        Long userId = 1L;
        Long categoryId = 1L;

        // 사용자 A의 BlogPage
        User userA = buildUser(userId);
        BlogPage blogPageA = buildBlogPage(blogPageId, userA);

        // 카테고리는 사용자 B의 BlogPage에 속함
        User userB = buildUser(2L);
        BlogPage blogPageB = buildBlogPage(2L, userB);

        Category category = buildCategory(categoryId, blogPageB);

        UpdateCategoryRequestDto requestDto =
                new UpdateCategoryRequestDto("updateCategory", "updateDescription");

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.of(blogPageA));
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        CustomException exception = assertThrows(CustomException.class,
                () -> categoryService.updateCategory(userId, categoryId, requestDto));

        assertEquals(FORBIDDEN_CATEGORY, exception.getErrorCode());
    }

    @Test
    @DisplayName("Category 삭제 실패 - 존재하지 않는 Category")
    void deleteCategoryFailedNotFound() {
        Long userId = 1L;
        Long categoryId = 1L;

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.of(buildBlogPage(1L)));

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> categoryService.deleteCategoryByCategoryId(userId, categoryId));

        assertEquals(CATEGORY_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    @DisplayName("Category 수정 실패 - 존재하는 카테고리 명")
    void updateCategoryFailedDuplicatedCategoryName() {
        // given
        Long userId = 1L;
        Long blogPageId = 1L;
        Long categoryId = 1L;

        User user = buildUser(userId);
        BlogPage blogPage = buildBlogPage(blogPageId, user);
        Category category = buildCategory(categoryId, blogPage);

        UpdateCategoryRequestDto requestDto =
                new UpdateCategoryRequestDto("updateCategory", "updateDescription");

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.of(blogPage));
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));
        when(categoryRepository.existsByBlogPage_IdAndName(blogPageId, requestDto.name()))
                .thenReturn(true);

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> categoryService.updateCategory(userId, categoryId, requestDto));

        assertEquals(DUPLICATED_CATEGORY_NAME, exception.getErrorCode());
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    void deleteCategorySuccess() {
        // given
        Long blogPageId = 1L;
        Long userId = 1L;
        Long categoryId = 1L;

        User user = buildUser(userId);
        BlogPage blogPage = buildBlogPage(blogPageId, user);
        Category category = buildCategory(categoryId, blogPage);

        when(blogPageRepository.getBlogPageByUser_UserId(userId))
                .thenReturn(Optional.of(blogPage));
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        // when
        categoryService.deleteCategoryByCategoryId(userId, categoryId);

        // then
        verify(categoryRepository, times(1)).delete(category);
    }

    private User buildUser(Long userId) {
        return User.builder()
                .userId(userId)
                .build();
    }


    private BlogPage buildBlogPage(Long blogPageId) {
        return BlogPage.builder()
                .id(blogPageId)
                .build();
    }

    private BlogPage buildBlogPage(Long blogPageId, User user) {
        return BlogPage.builder()
                .id(blogPageId)
                .user(user)
                .build();
    }

    private Category buildCategory(Long categoryId, BlogPage blogPage) {
        return Category.builder()
                .id(categoryId)
                .name("Category " + categoryId)
                .description("No." + categoryId + "category")
                .blogPage(blogPage)
                .build();
    }
}