package com.demosocket.blog.dto;

import lombok.Data;
import com.demosocket.blog.model.Status;
import com.demosocket.blog.model.Article;

import java.util.Set;

@Data
public class ArticleNewDto {

    private String title;
    private String text;
    private Status status;
    private Set<String> tags;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .text(text)
                .status(status)
                .build();
    }
}
