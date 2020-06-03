package com.demosocket.blog.dto;

import lombok.Data;
import com.demosocket.blog.model.Status;

@Data
public class ArticleEditDto {

    private String title;
    private String text;
    private Status status;
}
