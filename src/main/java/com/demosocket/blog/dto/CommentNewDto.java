package com.demosocket.blog.dto;

import lombok.Data;
import com.demosocket.blog.model.Comment;

import java.util.Date;

@Data
public class CommentNewDto {

    private String message;

    public Comment toEntity() {
        return Comment.builder()
                .message(message)
                .createdAt(new Date())
                .build();
    }
}
