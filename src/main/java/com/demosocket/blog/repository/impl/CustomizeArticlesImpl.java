package com.demosocket.blog.repository.impl;

import com.demosocket.blog.dto.SearchParametersDto;
import com.demosocket.blog.exception.UserNotFoundException;
import com.demosocket.blog.model.*;
import com.demosocket.blog.repository.CustomizeArticles;
import com.demosocket.blog.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class CustomizeArticlesImpl implements CustomizeArticles {

    private final EntityManager entityManager;
    private final UserRepository userRepository;

    @Override
    public Page<Article> findCriteriaArticles(SearchParametersDto params) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Article> criteriaQuery = cb.createQuery(Article.class);
        Root<Article> articleRoot = criteriaQuery.from(Article.class);
        Predicate criteria = cb.conjunction();

//        article status
        Predicate predicateStatus = cb.equal(articleRoot.get(Article_.status), Status.PUBLIC);
        criteria = cb.and(criteria, predicateStatus);

//        find by tags
        if (params.getTags() != null) {
            Join<Article, Tag> tagJoin = articleRoot.join(Article_.tags);
            Expression<String> tagExpression = tagJoin.get(Tag_.name.getName());
            Predicate predicateTag = tagExpression.in(params.getTags());
            criteria = cb.and(criteria, predicateTag);
        }

//        find by title
        if (params.getTitle() != null) {
            Predicate predicateTitle = cb.equal(articleRoot.get(Article_.title), params.getTitle());
            criteria = cb.and(criteria, predicateTitle);
        }

//        find by user
        if (params.getUserId() != null) {
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
}
