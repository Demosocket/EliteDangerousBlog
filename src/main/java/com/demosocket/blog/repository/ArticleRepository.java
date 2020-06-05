package com.demosocket.blog.repository;

import com.demosocket.blog.model.Tag;
import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Status;
import com.demosocket.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    List<Article> findAllByStatusAndUserAndTitleAndTagsIsContaining(Status status, User user, String title, Tag tag);

    List<Article> findAllByStatusAndTitleAndTagsIsContaining(Status status, String title, Tag tag);

    List<Article> findAllByStatusAndUserAndTagsIsContaining(Status status, User user, Tag tag);

    List<Article> findAllByStatusAndTagsIsContaining(Status status, Tag tag);

    Page<Article> findAllByUser(User user, Pageable pageable);

    List<Article> findAllByTags(Tag tag);
}
