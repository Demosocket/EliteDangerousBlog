package com.demosocket.blog.controller;

import com.demosocket.blog.model.Tag;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

//    @GetMapping("/articles")
//    public ResponseEntity<List<Article>> findArticleByTag(@RequestParam List<Tag> tags) {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @GetMapping("/tags-cloud")
    public ResponseEntity<Map<String, Integer>> cloudOfTags() {
        return new ResponseEntity<>(tagService.countArticlesWithTag(), HttpStatus.OK);
    }
}
