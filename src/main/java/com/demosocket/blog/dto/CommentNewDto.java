package com.demosocket.blog.dto;

import com.demosocket.blog.model.Comment;
import lombok.Data;

@Data
public class CommentNewDto {

    private String message;

    public Comment toEntity() {
        return Comment.builder()
                .message(message)
                .build();
    }
}
