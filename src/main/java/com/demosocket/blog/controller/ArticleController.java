package com.demosocket.blog.controller;

import com.demosocket.blog.dto.ArticleEditDto;
import com.demosocket.blog.dto.ArticleNewDto;
import com.demosocket.blog.model.Article;
import com.demosocket.blog.security.jwt.JwtTokenUtil;
import com.demosocket.blog.service.ArticleService;
import com.demosocket.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        Article article = articleNewDto.toEntity();
        article.setUser(userService.findByEmail(email));
        articleService.saveArticle(article);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Article>> getAllPublicArticles() {
        return new ResponseEntity<>(articleService.findAllPublic(), HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Article>> getYourOwnArticles(HttpServletRequest request) {
        final String email = jwtTokenUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));

        return new ResponseEntity<>(articleService.findAllByUser(userService.findByEmail(email)), HttpStatus.OK);
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
