package com.demosocket.blog.dto;

import com.demosocket.blog.model.Status;
import lombok.Data;

@Data
public class ArticleEditDto {

    private String title;
    private String article;
    private Status status;
}
