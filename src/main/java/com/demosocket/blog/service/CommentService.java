package com.demosocket.blog.service;

import com.demosocket.blog.dto.CommentNewDto;
import com.demosocket.blog.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Comment findComment(String email, Integer articleId, Integer commentId);

    void deleteComment(String email, Integer articleId, Integer commentId);

    void saveNewComment(CommentNewDto commentNewDto, String email, Integer articleId);

    Page<Comment> findAllCommentsFromArticle(String email, Integer articleId, Integer userId, Pageable pageable);
}
