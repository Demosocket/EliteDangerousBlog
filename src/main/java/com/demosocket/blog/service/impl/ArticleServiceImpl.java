package com.demosocket.blog.service.impl;

import com.demosocket.blog.dto.ArticleEditDto;
import com.demosocket.blog.exception.ArticleNotFoundException;
import com.demosocket.blog.exception.PermissionDeniedArticleAccessException;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.model.Status;
import com.demosocket.blog.model.User;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> findAllPublic() {
        return articleRepository.findAllByStatus(Status.PUBLIC);
    }

    @Override
    public List<Article> findAllByUser(User user) {
        return articleRepository.findAllByUser(user);
    }

    @Override
    public void saveArticle(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void checkAndDeleteArticle(Integer id, String email) {
        Article article = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        User user = userRepository.findByEmail(email);
        if (article.getUser().equals(user)) {
           articleRepository.delete(article);
        } else {
            throw new PermissionDeniedArticleAccessException();
        }
    }

    @Override
    public void checkAndEditArticle(Integer id, String email, ArticleEditDto articleEditDto) {
        Article article = articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
        User user = userRepository.findByEmail(email);
        if (article.getUser().equals(user)) {
            article.setTitle(articleEditDto.getTitle());
            article.setArticle(articleEditDto.getArticle());
            article.setStatus(articleEditDto.getStatus());
            article.setUpdatedAt(new Date());
            articleRepository.save(article);
        } else {
            throw new PermissionDeniedArticleAccessException();
        }
    }
}
