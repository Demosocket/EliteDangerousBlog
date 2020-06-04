package com.demosocket.blog.controller;

import com.demosocket.blog.model.Article;
import com.demosocket.blog.dto.ArticleNewDto;
import com.demosocket.blog.dto.ArticleEditDto;
import com.demosocket.blog.service.UserService;
import com.demosocket.blog.service.ArticleService;
import com.demosocket.blog.security.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ArticleService articleService;

    @Autowired
    public ArticleController(UserService userService,
                             JwtTokenUtil jwtTokenUtil,
                             ArticleService articleService) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.articleService = articleService;
    }

    @PostMapping()
    public ResponseEntity<?> addArticle(@RequestBody ArticleNewDto articleNewDto, HttpServletRequest request) {
        final String email = jwtTokenUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));
        articleService.saveArticle(articleNewDto, email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<Article>> getAllPublicArticles(@RequestParam("skip") Integer page,
                                                              @RequestParam("limit") Integer size,
                                                              @RequestParam("q") String title,
                                                              @RequestParam("author") Integer userId,
                                                              @RequestParam("sort") String field,
                                                              @RequestParam("order") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(order), field);
        Page<Article> articlePage = articleService.findAllPublic(title, userId, pageable);

        return new ResponseEntity<>(articlePage, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<Article>> getYourOwnArticles(HttpServletRequest request,
                                                            @RequestParam("skip") Integer page,
                                                            @RequestParam("limit") Integer size,
                                                            @RequestParam("sort") String field,
                                                            @RequestParam("order") String order) {
        final String email = jwtTokenUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(order), field);
        Page<Article> articlePage = articleService.findAllByUser(userService.findByEmail(email), pageable);

        return new ResponseEntity<>(articlePage, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editYourOwnArticle(@PathVariable Integer id,
                                                HttpServletRequest request,
                                                @RequestBody ArticleEditDto articleEditDto) {
        final String email = jwtTokenUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));
        articleService.checkAndEditArticle(id, email, articleEditDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteYourOwnArticle(@PathVariable Integer id, HttpServletRequest request) {
        final String email = jwtTokenUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));
        articleService.checkAndDeleteArticle(id, email);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
