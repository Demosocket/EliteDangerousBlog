package com.demosocket.blog.repository;

import com.demosocket.blog.model.Article;
import com.demosocket.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByArticle(Article article);
    Optional<Comment> findByArticleAndId(Article article, Integer id);
}
