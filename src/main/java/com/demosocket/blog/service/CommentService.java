package com.demosocket.blog.service;

import com.demosocket.blog.model.Comment;
import org.springframework.data.domain.Page;
import com.demosocket.blog.dto.CommentNewDto;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Comment findComment(Integer articleId, Integer commentId);

    void deleteComment(String email, Integer articleId, Integer commentId);

    void saveNewComment(CommentNewDto commentNewDto, String email, Integer articleId);

    Page<Comment> findAllCommentsFromArticle(Integer articleId, Integer userId, Pageable pageable);
}
