package com.demosocket.blog.dto;

import lombok.Data;
import com.demosocket.blog.model.Status;

import java.util.Set;

@Data
public class ArticleEditDto {

    private String title;
    private String text;
    private Status status;
    private Set<String> tags;
}
