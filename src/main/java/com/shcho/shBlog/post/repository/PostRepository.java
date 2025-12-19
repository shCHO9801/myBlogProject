package com.shcho.shBlog.post.repository;

import com.shcho.shBlog.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
