package com.demosocket.blog.controller;

import org.springframework.http.HttpStatus;
import com.demosocket.blog.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@RestController
@RequestMapping
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags-cloud")
    public ResponseEntity<Map<String, Integer>> cloudOfTags() {
        return new ResponseEntity<>(tagService.countArticlesWithTag(), HttpStatus.OK);
    }
}
