package com.demosocket.blog.dto;

import com.demosocket.blog.model.Article;
import com.demosocket.blog.model.Status;
import lombok.Data;

import java.util.Date;

@Data
public class ArticleNewDto {

    private String title;
    private String article;
    private Status status;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .article(article)
                .status(status)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }
}
