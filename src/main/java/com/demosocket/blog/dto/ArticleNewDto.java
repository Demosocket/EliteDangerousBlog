package com.demosocket.blog.dto;

import com.demosocket.blog.model.Article;
import com.demosocket.blog.model.Status;
import lombok.Data;

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
