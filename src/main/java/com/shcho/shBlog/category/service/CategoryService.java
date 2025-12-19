package com.shcho.shBlog.category.service;

import com.shcho.shBlog.blogpage.entity.BlogPage;
import com.shcho.shBlog.blogpage.repository.BlogPageRepository;
import com.shcho.shBlog.category.dto.CreateCategoryRequestDto;
import com.shcho.shBlog.category.dto.UpdateCategoryRequestDto;
import com.shcho.shBlog.category.entity.Category;
import com.shcho.shBlog.category.repository.CategoryRepository;
import com.shcho.shBlog.libs.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.shcho.shBlog.libs.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BlogPageRepository blogPageRepository;

    public Category createCategory(Long userId, CreateCategoryRequestDto requestDto) {

        BlogPage blogPage = getBlogPageByUserId(userId);

        validateDuplicateName(blogPage.getId(), requestDto.name());

        return categoryRepository.save(
                Category.of(blogPage, requestDto.name(), requestDto.description())
        );
    }

    public List<Category> getAllCategoriesByBlogPageId(Long blogPageId) {

        blogPageRepository.findById(blogPageId)
                .orElseThrow(() -> new CustomException(BLOG_PAGE_NOT_FOUND));

        return categoryRepository.findAllByBlogPage_IdOrderByNameAsc(blogPageId);
    }

    @Transactional
    public Category updateCategory(Long userId, Long categoryId, UpdateCategoryRequestDto requestDto) {

        BlogPage userBlogPage = getBlogPageByUserId(userId);
        Category category = findCategoryById(categoryId);

        validateCategoryOwner(category, userBlogPage);

        if (!category.getName().equals(requestDto.name())) {
            validateDuplicateName(userBlogPage.getId(), requestDto.name());
        }

        category.updateCategory(requestDto.name(), requestDto.description());

        return categoryRepository.save(category);
    }


    @Transactional
    public void deleteCategoryByCategoryId(Long userId, Long categoryId) {

        BlogPage userBlogPage = getBlogPageByUserId(userId);
        Category category = findCategoryById(categoryId);

        validateCategoryOwner(category, userBlogPage);

        categoryRepository.delete(category);
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(CATEGORY_NOT_FOUND));
    }

    private BlogPage getBlogPageByUserId(Long userId) {
        return blogPageRepository.getBlogPageByUser_UserId(userId)
                .orElseThrow(() -> new CustomException(BLOG_PAGE_NOT_FOUND));
    }

    private void validateCategoryOwner(Category category, BlogPage userBlogPage) {
        if (!category.getBlogPage().getId().equals(userBlogPage.getId())) {
            throw new CustomException(FORBIDDEN_CATEGORY);
        }
    }

    private void validateDuplicateName(Long blogPageId, String categoryName) {
        if (categoryRepository.existsByBlogPage_IdAndName(blogPageId, categoryName)) {
            throw new CustomException(DUPLICATED_CATEGORY_NAME);
        }
    }
}
