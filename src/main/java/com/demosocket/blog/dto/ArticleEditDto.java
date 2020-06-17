package com.demosocket.blog.dto;

import com.demosocket.blog.model.Status;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
public class ArticleEditDto {

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    private Status status;

    private Set<String> tags;
}
