package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.Tag;
import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Status;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.dto.ArticleNewDto;
import com.demosocket.blog.dto.ArticleEditDto;
import com.demosocket.blog.repository.TagRepository;
import com.demosocket.blog.service.ArticleService;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.exception.UserNotFoundException;
import com.demosocket.blog.exception.ArticleNotFoundException;
import com.demosocket.blog.exception.PermissionDeniedArticleAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(TagRepository tagRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Page<Article> findAllPublic(String title, Integer userId, Pageable pageable) {
        Page<Article> articlePage;
        if (userId > 0) {
            User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            articlePage = !title.equals("all")
                    ? articleRepository.findAllByStatusAndUserAndTitle(Status.PUBLIC, user, title, pageable)
                    : articleRepository.findAllByStatusAndUser(Status.PUBLIC, user, pageable);
        } else {
            articlePage = !title.equals("all")
                    ? articleRepository.findAllByStatusAndTitle(Status.PUBLIC, title, pageable)
                    : articleRepository.findAllByStatus(Status.PUBLIC, pageable);
        }
        return articlePage;
    }

    @Override
    public Page<Article> findAllByUser(User user, Pageable pageable) {
        return articleRepository.findAllByUser(user, pageable);
    }

    @Override
    public void saveArticle(ArticleNewDto articleNewDto, String email) {
        Article article = articleNewDto.toEntity();
        article.setUser(userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new));
//        tags
        Set<String> tags = articleNewDto.getTags();
        Set<Tag> tagsForArticle = new HashSet<>();
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
        articleRepository.save(article);
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
            articleRepository.save(article);
        } else {
            throw new PermissionDeniedArticleAccessException();
        }
    }
}
