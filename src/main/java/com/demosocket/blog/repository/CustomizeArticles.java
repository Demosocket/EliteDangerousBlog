package com.demosocket.blog.repository;

import com.demosocket.blog.dto.SearchParametersDto;
import com.demosocket.blog.model.Article;
import org.springframework.data.domain.Page;

public interface CustomizeArticles {

    Page<Article> findCriteriaArticles(SearchParametersDto parametersDto);
}
