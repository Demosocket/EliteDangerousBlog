package com.demosocket.blog.service.impl;

import com.demosocket.blog.dto.CommentNewDto;
import com.demosocket.blog.exception.*;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.model.Comment;
import com.demosocket.blog.model.Status;
import com.demosocket.blog.model.User;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.repository.CommentRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(UserRepository userRepository,
                              ArticleRepository articleRepository,
                              CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment findComment(String email, Integer articleId, Integer commentId) {
        User userFinder = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
//        check if article hasn't PRIVATE Status
        if (checkPermissions(article, userFinder)) {
            return commentRepository.findByArticleAndId(article, commentId).orElseThrow(CommentNotFoundException::new);
        } else {
            throw new PermissionDeniedArticleAccessException();
        }
    }

    @Override
    public void deleteComment(String email, Integer articleId, Integer commentId) {
        User userDeleter = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
//        check if deleter is articleOwner or commentOwner
        if (userDeleter.equals(article.getUser()) || userDeleter.equals(comment.getUser())) {
            commentRepository.deleteById(commentId);
        } else {
            throw new PermissionDeniedCommentAccessException();
        }
    }

    public void saveNewComment(CommentNewDto commentNewDto, String email, Integer articleId) {
        Comment comment = commentNewDto.toEntity();
        User userWriter = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        comment.setUser(userWriter);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
//        check if article hasn't PRIVATE Status
        if (checkPermissions(article, userWriter)) {
            comment.setArticle(articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new));
            commentRepository.save(comment);
        } else {
            throw new PermissionDeniedArticleAccessException();
        }
    }

    @Override
    public Page<Comment> findAllCommentsFromArticle(String email, Integer articleId, Integer userId, Pageable pageable) {
        User userFinder = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        Page<Comment> commentPage;
//        if @RequestParam("userId") == 'null' then we should find all articles
        if (userId == null) {
            if (checkPermissions(article, userFinder)) {
                commentPage = commentRepository.findAllByArticle(article, pageable);
            } else {
                throw new PermissionDeniedArticleAccessException();
            }
        } else {
            if (checkPermissions(article, userFinder)) {
                User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
                commentPage = commentRepository.findAllByUserAndArticle(user, article, pageable);
            } else {
                throw new PermissionDeniedArticleAccessException();
            }
        }
        return commentPage;
    }

    private boolean checkPermissions(Article article, User user) {
//        user can comment public or own article
        return article.getStatus().equals(Status.PUBLIC)
                || (article.getStatus().equals(Status.PRIVATE) && article.getUser().equals(user));
    }
}
