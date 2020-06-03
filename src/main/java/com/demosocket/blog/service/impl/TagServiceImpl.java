package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.Tag;
import com.demosocket.blog.service.TagService;
import com.demosocket.blog.repository.TagRepository;
import com.demosocket.blog.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, ArticleRepository articleRepository) {
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Map<String, Integer> countArticlesWithTag() {
        return tagRepository.findAll().stream()
                .collect(Collectors.toMap(Tag::getName, tag -> articleRepository.findAllByTags(tag).size()));
    }
}
