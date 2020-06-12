package com.demosocket.blog.dto;

import com.demosocket.blog.model.Comment;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentNewDto {

    @NotBlank
    private String message;

    public Comment toEntity() {
        return Comment.builder()
                .message(message)
                .build();
    }
}
