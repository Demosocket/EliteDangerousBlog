package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.Tag;
import com.demosocket.blog.model.User;
import com.demosocket.blog.model.Status;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.dto.ArticleNewDto;
import com.demosocket.blog.dto.ArticleEditDto;
import com.demosocket.blog.service.ArticleService;
import com.demosocket.blog.dto.SearchParametersDto;
import com.demosocket.blog.repository.TagRepository;
import com.demosocket.blog.repository.UserRepository;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.exception.TagNotFoundException;
import com.demosocket.blog.exception.UserNotFoundException;
import com.demosocket.blog.exception.ArticleNotFoundException;
import com.demosocket.blog.exception.PermissionDeniedArticleAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(TagRepository tagRepository,
                              UserRepository userRepository,
                              ArticleRepository articleRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Page<Article> findAllPublic(SearchParametersDto params) {

        Set<Article> articleSet = new HashSet<>();
//        if @RequestParam("tags") is 'null' then we should find with all tags
        if (params.getTags() == null) {
            params.setTags(tagRepository.findAll().stream().map(Tag::getName).collect(Collectors.toList()));
        }

        if (params.getUserId() != null) {
//            if @RequestParam("tags") != 'null' then we should find by user
            User user = userRepository.findById(params.getUserId()).orElseThrow(UserNotFoundException::new);
            for (String tagName : params.getTags()) {
                Tag tag = tagRepository.findByName(tagName).orElseThrow(TagNotFoundException::new);
                if (params.getTitle() != null) {
                    articleSet.addAll(articleRepository.findAllByStatusAndUserAndTitleAndTagsIsContaining(
                            Status.PUBLIC, user, params.getTitle(), tag));
                } else {
                    articleSet.addAll(articleRepository.findAllByStatusAndUserAndTagsIsContaining(
                            Status.PUBLIC, user, tag));
                }
            }
        } else {
//            else we don't have a user_id from @RequestParam
            for (String tagName : params.getTags()) {
                Tag tag = tagRepository.findByName(tagName).orElseThrow(TagNotFoundException::new);
                if (params.getTitle() != null) {
                    articleSet.addAll(articleRepository.findAllByStatusAndTitleAndTagsIsContaining(
                            Status.PUBLIC, params.getTitle(), tag));
                } else {
                    articleSet.addAll(articleRepository.findAllByStatusAndTagsIsContaining(
                            Status.PUBLIC, tag));
                }
            }
        }
//        pagination
        int start = params.getPage() * params.getSize();
        int end = Math.min((start + params.getSize()), articleSet.size());

        List<Article> articleList = new ArrayList<>(articleSet);

//        sort by 'field' with 'order'
        switch (params.getSortField()) {
            case "id":
                articleList = params.getOrder().equals("asc")
                        ? articleList.stream().sorted(Comparator.comparing(Article::getId))
                            .collect(Collectors.toList())
                        : articleList.stream().sorted(Comparator.comparing(Article::getId).reversed())
                            .collect(Collectors.toList());
                break;
            case "title":
                articleList = params.getOrder().equals("asc")
                        ? articleList.stream().sorted(Comparator.comparing(Article::getTitle))
                            .collect(Collectors.toList())
                        : articleList.stream().sorted(Comparator.comparing(Article::getTitle).reversed())
                            .collect(Collectors.toList());
                break;
            case "text":
                articleList = params.getOrder().equals("asc")
                        ? articleList.stream().sorted(Comparator.comparing(Article::getText))
                            .collect(Collectors.toList())
                        : articleList.stream().sorted(Comparator.comparing(Article::getText).reversed())
                            .collect(Collectors.toList());
                break;
            case "created_at":
                articleList = params.getOrder().equals("asc")
                        ? articleList.stream().sorted(Comparator.comparing(Article::getCreatedAt))
                            .collect(Collectors.toList())
                        : articleList.stream().sorted(Comparator.comparing(Article::getCreatedAt).reversed())
                            .collect(Collectors.toList());
                break;
            case "updated_at":
                articleList = params.getOrder().equals("asc")
                        ? articleList.stream().sorted(Comparator.comparing(Article::getUpdatedAt))
                            .collect(Collectors.toList())
                        : articleList.stream().sorted(Comparator.comparing(Article::getUpdatedAt).reversed())
                            .collect(Collectors.toList());
                break;
        }
//        copy the articleList into resultList by page
        List<Article> resultList = new ArrayList<>();
        if (start <= end) {
            resultList = articleList.subList(start, end);
        }
//        'PageImpl' work with 'Pageable' but it's fiction
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
