package com.shcho.shBlog.blogpage.repository;

import com.shcho.shBlog.blogpage.entity.BlogPage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPageRepository extends JpaRepository<BlogPage, Long> {
}
