package com.shcho.shBlog.blogpage.repository;

import com.shcho.shBlog.blogpage.entity.BlogPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogPageRepository extends JpaRepository<BlogPage, Long> {
    Optional<BlogPage> getBlogPageByUser_UserId(Long userUserId);
}
