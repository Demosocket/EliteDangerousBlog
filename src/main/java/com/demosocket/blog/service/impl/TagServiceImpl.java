package com.demosocket.blog.service.impl;

import com.demosocket.blog.dto.TagsCountDto;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.repository.TagRepository;
import com.demosocket.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public List<TagsCountDto> countArticlesWithTag() {
        return tagRepository.findAll().stream().map(tag -> new TagsCountDto(tag.getName(),
                        articleRepository.findAllByTags(tag).size())).collect(Collectors.toList());
    }
}
