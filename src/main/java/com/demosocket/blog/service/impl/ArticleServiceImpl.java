package com.demosocket.blog.service.impl;

import com.demosocket.blog.dto.ArticleEditDto;
import com.demosocket.blog.dto.ArticleNewDto;
import com.demosocket.blog.dto.SearchParametersDto;
import com.demosocket.blog.exception.ArticleNotFoundException;
import com.demosocket.blog.exception.PermissionDeniedArticleAccessException;
import com.demosocket.blog.exception.UserNotFoundException;
import com.demosocket.blog.model.*;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.repository.CustomizeArticles;
import com.demosocket.blog.repository.TagRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArticleServiceImpl implements ArticleService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CustomizeArticles customizeArticles;

    @Override
    public Page<Article> findAllPublic(SearchParametersDto params) {
        return customizeArticles.findCriteriaArticles(params);
    }

    @Override
    public Page<Article> findAllByUser(User user, Pageable pageable) {
        return articleRepository.findAllByUser(user, pageable);
    }

    @Override
    public void saveArticle(ArticleNewDto articleNewDto, String email) {
        Article article = articleNewDto.toEntity();
        article.setUser(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
//        check tags in db and save
        articleRepository.save(checkTagsInDb(article, articleNewDto.getTags()));
    }

    @Override
    public void checkAndDeleteArticle(Integer id, String email) {
        Article article = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
//        check if user wrote this article
        if (article.getUser().equals(user)) {
            articleRepository.delete(article);
        } else {
            throw new PermissionDeniedArticleAccessException();
        }
    }

    @Override
    public void checkAndEditArticle(Integer id, String email, ArticleEditDto articleEditDto) {
        Article article = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
//        check if user wrote this article
        if (article.getUser().equals(user)) {
            article.setTitle(articleEditDto.getTitle());
            article.setText(articleEditDto.getText());
            article.setStatus(articleEditDto.getStatus());
            article.setUpdatedAt(new Date());
//            check tags in db and save
            articleRepository.save(checkTagsInDb(article, articleEditDto.getTags()));
        } else {
            throw new PermissionDeniedArticleAccessException();
        }
    }

    private Article checkTagsInDb(Article article, Set<String> tags) {
//        result tags for article
        Set<Tag> tagsForArticle = new HashSet<>();
//        check if tag already exist
        for (String tag : tags) {
            Optional<Tag> tagFromDb = tagRepository.findByName(tag);
            if (!tagFromDb.isPresent()) {
                Tag tagToDb = new Tag();
                tagToDb.setName(tag);
                tagRepository.save(tagToDb);
                tagsForArticle.add(tagToDb);
            } else {
                tagsForArticle.add(tagFromDb.get());
            }
        }
        article.setTags(tagsForArticle);
        return article;
    }
}
