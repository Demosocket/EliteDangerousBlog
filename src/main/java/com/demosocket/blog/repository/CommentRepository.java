package com.demosocket.blog.repository;

import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Comment;
import com.demosocket.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findAllByUserAndArticle(User user, Article article, Pageable pageable);

    Optional<Comment> findByArticleAndId(Article article, Integer id);
}
