package com.demosocket.blog.repository;

import com.demosocket.blog.model.Tag;
import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Status;
import com.demosocket.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Status> {

    Page<Article> findAllByStatusAndUserAndTitle(Status status, User user, String title, Pageable pageable);
    Page<Article> findAllByStatusAndTitle(Status status, String title, Pageable pageable);
    Page<Article> findAllByStatusAndUser(Status status, User user, Pageable pageable);
    Page<Article> findAllByStatus(Status status, Pageable pageable);
    Page<Article> findAllByUser(User user, Pageable pageable);
    List<Article> findAllByTags(Tag tag);
}
