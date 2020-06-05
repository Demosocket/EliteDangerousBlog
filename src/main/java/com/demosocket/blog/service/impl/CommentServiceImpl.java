package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.model.Comment;
import com.demosocket.blog.dto.CommentNewDto;
import com.demosocket.blog.service.CommentService;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.repository.CommentRepository;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.exception.UserNotFoundException;
import com.demosocket.blog.exception.CommentNotFoundException;
import com.demosocket.blog.exception.ArticleNotFoundException;
import com.demosocket.blog.exception.PermissionDeniedCommentAccessException;
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
    public Comment findComment(Integer articleId, Integer commentId) {
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        return commentRepository.findByArticleAndId(article, commentId).orElseThrow(CommentNotFoundException::new);
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
        comment.setUser(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
        comment.setArticle(articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new));

        commentRepository.save(comment);
    }

    @Override
    public Page<Comment> findAllCommentsFromArticle(Integer articleId, Integer userId, Pageable pageable) {
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return commentRepository.findAllByUserAndArticle(user, article, pageable);
    }
}
