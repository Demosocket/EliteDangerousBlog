package com.demosocket.blog.service;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.dto.ArticleEditDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    Page<Article> findAllPublic(String articleTitle, Integer userId, Pageable pageable);
    Page<Article> findAllByUser(User user, Pageable pageable);
    void saveArticle(Article article);
    void checkAndDeleteArticle(Integer id, String email);
    void checkAndEditArticle(Integer id, String email, ArticleEditDto articleEditDto);
}
