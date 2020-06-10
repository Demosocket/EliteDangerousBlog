package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.*;
import com.demosocket.blog.dto.ArticleNewDto;
import com.demosocket.blog.dto.ArticleEditDto;
import com.demosocket.blog.service.ArticleService;
import com.demosocket.blog.dto.SearchParametersDto;
import com.demosocket.blog.repository.TagRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.exception.UserNotFoundException;
import com.demosocket.blog.exception.ArticleNotFoundException;
import com.demosocket.blog.exception.PermissionDeniedArticleAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.util.*;
import javax.persistence.criteria.*;
import javax.persistence.EntityManager;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final EntityManager entityManager;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(EntityManager entityManager,
                              TagRepository tagRepository,
                              UserRepository userRepository,
                              ArticleRepository articleRepository) {
        this.entityManager = entityManager;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Page<Article> findAllPublic(SearchParametersDto params) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Article> criteriaQuery = cb.createQuery(Article.class);
        Root<Article> articleRoot = criteriaQuery.from(Article.class);
        Predicate criteria = cb.conjunction();

//        article status
        Predicate predicateStatus = cb.equal(articleRoot.get(Article_.status), Status.PUBLIC);
        criteria = cb.and(criteria, predicateStatus);

//        find by tags
        if (params.getTags()!=null) {
            Join<Article, Tag> tagJoin = articleRoot.join(Article_.tags);
            Expression<String> tagExpression = tagJoin.get(Tag_.name.getName());
            Predicate predicateTag = tagExpression.in(params.getTags());
            criteria = cb.and(criteria, predicateTag);
        }

//        find by title
        if (params.getTitle()!=null) {
            Predicate predicateTitle = cb.equal(articleRoot.get(Article_.title), params.getTitle());
            criteria = cb.and(criteria, predicateTitle);
        }

//        find by user
        if (params.getUserId()!=null) {
            Predicate predicateUser = cb.equal(articleRoot.get(Article_.user),
                    userRepository.findById(params.getUserId()).orElseThrow(UserNotFoundException::new));
            criteria = cb.and(criteria, predicateUser);
        }

//        sort
        if (Sort.Direction.fromString(params.getOrder()).isAscending()) {
            criteriaQuery.orderBy(cb.asc(articleRoot.get(params.getSortField())));
        } else {
            criteriaQuery.orderBy(cb.desc(articleRoot.get(params.getSortField())));
        }

        criteriaQuery.select(articleRoot).where(criteria);
        List<Article> articleList = entityManager.createQuery(criteriaQuery).getResultList();

//        pagination
        int start = params.getPage() * params.getSize();
        int end = Math.min((start + params.getSize()), articleList.size());

//        copy the articleList into resultList by page
        List<Article> resultList = new ArrayList<>();
        if (start <= end) {
            resultList = articleList.subList(start, end);
        }

//        'PageImpl' work with 'Pageable'
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(),
                Sort.Direction.fromString(params.getOrder()), params.getSortField());

        return new PageImpl<>(resultList, pageable, articleList.size());
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
