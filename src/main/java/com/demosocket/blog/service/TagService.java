package com.demosocket.blog.service;

import com.demosocket.blog.dto.TagsCountDto;

import java.util.List;

public interface TagService {

    List<TagsCountDto> countArticlesWithTag();
}
