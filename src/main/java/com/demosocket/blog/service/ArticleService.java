package com.demosocket.blog.service;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.dto.ArticleNewDto;
import com.demosocket.blog.dto.ArticleEditDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ArticleService {

    Page<Article> findAllPublic(String articleTitle, Integer userId, Pageable pageable);

    Page<Article> findAllByUser(User user, Pageable pageable);

    void saveArticle(ArticleNewDto articleNewDto, String email);

    void checkAndDeleteArticle(Integer id, String email);

    void checkAndEditArticle(Integer id, String email, ArticleEditDto articleEditDto);
}
