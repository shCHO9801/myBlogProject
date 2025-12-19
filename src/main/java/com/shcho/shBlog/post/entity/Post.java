package com.shcho.shBlog.post.entity;

import com.shcho.shBlog.blogpage.entity.BlogPage;
import com.shcho.shBlog.category.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean published;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_page_id", nullable = false)
    private BlogPage blogPage;

    // 초안 생성
    public static Post createDraft(
            BlogPage blogPage,
            Category category,
            String title,
            String content
    ) {
        return Post.builder()
                .title(title)
                .content(content)
                .category(category)
                .published(false)
                .blogPage(blogPage)
                .build();
    }

    public void publish() {
        this.published = true;
    }

    public void unpublish() {
        this.published = false;
    }

    public void update(String title, String content, Category category) {
        this.title = title;
        this.content = content;
        this.category = category;
    }
}
