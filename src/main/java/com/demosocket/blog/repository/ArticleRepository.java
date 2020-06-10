package com.demosocket.blog.repository;

import com.demosocket.blog.model.Tag;
import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Page<Article> findAllByUser(User user, Pageable pageable);

    List<Article> findAllByTags(Tag tag);
}
