package com.demosocket.blog.repository;

import com.demosocket.blog.model.Article;
import com.demosocket.blog.model.Status;
import com.demosocket.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    List<Article> findAllByStatus(Status status);
    List<Article> findAllByUser(User user);
}
