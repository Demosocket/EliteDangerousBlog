package com.demosocket.blog.dto;

import com.demosocket.blog.model.Status;
import lombok.Data;

import java.util.Set;

@Data
public class ArticleEditDto {

    private String title;
    private String text;
    private Status status;
    private Set<String> tags;
}
