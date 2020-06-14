package com.demosocket.blog.service.impl;

import com.demosocket.blog.model.Article;
import com.demosocket.blog.model.Tag;
import com.demosocket.blog.repository.ArticleRepository;
import com.demosocket.blog.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = TagServiceImpl.class)
class TagServiceImplTest {

    @MockBean
    private TagRepository tagRepository;

    @MockBean
    private ArticleRepository articleRepository;

    @Test
    void shouldCountArticlesWithTag() {
        Tag tag1 = Tag.builder().name("A").build();
        Tag tag2 = Tag.builder().name("B").build();
        Tag tag3 = Tag.builder().name("C").build();

        Article article1 = Article.builder().text("1").tags(new HashSet<>(Arrays.asList(tag1, tag2))).build();
        Article article2 = Article.builder().text("2").tags(new HashSet<>(Collections.singletonList(tag1))).build();

        Mockito.when(articleRepository.findAllByTags(tag1)).thenReturn(Arrays.asList(article1, article2));
        Mockito.when(articleRepository.findAllByTags(tag2)).thenReturn(Collections.singletonList(article2));
        Mockito.when(articleRepository.findAllByTags(tag3)).thenReturn(Collections.emptyList());

        assertEquals(2, articleRepository.findAllByTags(tag1).size());
        assertEquals(1, articleRepository.findAllByTags(tag2).size());
        assertEquals(0, articleRepository.findAllByTags(tag3).size());
    }
}
