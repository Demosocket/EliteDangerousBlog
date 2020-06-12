package com.demosocket.blog.service;

import com.demosocket.blog.dto.ArticleEditDto;
import com.demosocket.blog.dto.ArticleNewDto;
import com.demosocket.blog.dto.SearchParametersDto;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleService {

    Page<Article> findAllPublic(SearchParametersDto params);

    Page<Article> findAllByUser(User user, Pageable pageable);

    void saveArticle(ArticleNewDto articleNewDto, String email);

    void checkAndDeleteArticle(Integer id, String email);

    void checkAndEditArticle(Integer id, String email, ArticleEditDto articleEditDto);
}
