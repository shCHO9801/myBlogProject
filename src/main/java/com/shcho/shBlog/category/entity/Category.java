package com.shcho.shBlog.category.entity;

import com.shcho.shBlog.blogpage.entity.BlogPage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_page_id", nullable = false)
    private BlogPage blogPage;

    public static Category of(BlogPage blogPage, String name, String description) {
        return Category.builder()
                .blogPage(blogPage)
                .name(name)
                .description(description)
                .build();
    }

    public void updateCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
