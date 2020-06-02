package com.demosocket.blog.service;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.dto.ArticleEditDto;

import java.util.List;

public interface ArticleService {

    List<Article> findAllPublic();
    List<Article> findAllByUser(User user);
    void saveArticle(Article article);
    void checkAndDeleteArticle(Integer id, String email);
    void checkAndEditArticle(Integer id, String email, ArticleEditDto articleEditDto);
}
