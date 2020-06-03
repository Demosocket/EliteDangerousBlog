package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Status;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.dto.ArticleEditDto;
import com.demosocket.blog.service.ArticleService;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.exception.UserNotFoundException;
import com.demosocket.blog.exception.ArticleNotFoundException;
import com.demosocket.blog.exception.PermissionDeniedArticleAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.Date;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Page<Article> findAllPublic(String title, Integer userId, Pageable pageable) {
        Page<Article> articlePage;
        if (userId > 0) {
            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            if (!title.equals("all")) {
                articlePage = articleRepository.findAllByStatusAndUserAndTitle(Status.PUBLIC, user, title, pageable);
            } else {
                articlePage = articleRepository.findAllByStatusAndUser(Status.PUBLIC, user, pageable);
            }
        } else {
            if (!title.equals("all")) {
                articlePage = articleRepository.findAllByStatusAndTitle(Status.PUBLIC, title, pageable);
            } else {
                articlePage = articleRepository.findAllByStatus(Status.PUBLIC, pageable);
            }
        }
        return articlePage;
    }

    @Override
    public Page<Article> findAllByUser(User user, Pageable pageable) {
        return articleRepository.findAllByUser(user, pageable);
    }

    @Override
    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void checkAndDeleteArticle(Integer id, String email) {
        Article article = articleRepository.findById(id).orElseThrow(UserNotFoundException::new);
        User user = userRepository.findByEmail(email);
        if (article.getUser().equals(user)) {
           articleRepository.delete(article);
        } else {
            throw new PermissionDeniedArticleAccessException();
        }
    }

    @Override
    public void checkAndEditArticle(Integer id, String email, ArticleEditDto articleEditDto) {
        Article article = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        User user = userRepository.findByEmail(email);
        if (article.getUser().equals(user)) {
            article.setTitle(articleEditDto.getTitle());
            article.setText(articleEditDto.getText());
            article.setStatus(articleEditDto.getStatus());
            article.setUpdatedAt(new Date());
            articleRepository.save(article);
        } else {
            throw new PermissionDeniedArticleAccessException();
        }
    }
}
