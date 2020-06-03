package com.demosocket.blog.service;

import com.demosocket.blog.dto.CommentNewDto;
import com.demosocket.blog.model.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAllCommentsFromArticle(Integer articleId);
    Comment findComment(Integer articleId, Integer commentId);
    void deleteComment(String email, Integer articleId, Integer commentId);
    void saveNewComment(CommentNewDto commentNewDto, String email, Integer articleId);
}
