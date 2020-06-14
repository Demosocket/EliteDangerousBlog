package com.demosocket.blog.controller;

import com.demosocket.blog.dto.CommentNewDto;
import com.demosocket.blog.model.Comment;
import com.demosocket.blog.security.jwt.JwtTokenUtil;
import com.demosocket.blog.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.demosocket.blog.controller.ArticleController.AUTHORIZATION;

@RestController
@RequestMapping("/articles")
public class CommentController {

    private final JwtTokenUtil jwtTokenUtil;
    private final CommentService commentService;

    public CommentController(JwtTokenUtil jwtTokenUtil, CommentService commentService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.commentService = commentService;
    }

    @PostMapping("/{articleId}/comments")
    public ResponseEntity<?> addNewComment(@PathVariable Integer articleId,
                                           @RequestHeader(AUTHORIZATION) String token,
                                           @Valid @RequestBody CommentNewDto commentNewDto) {
        final String email = jwtTokenUtil.getEmailFromToken(token.substring(7));
        commentService.saveNewComment(commentNewDto, email, articleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<Page<Comment>> allCommentsFromArticle(@PathVariable Integer articleId,
                                                                @RequestParam("skip") Integer page,
                                                                @RequestParam("limit") Integer size,
                                                                @RequestParam(value = "author", required = false)
                                                                        Integer userId,
                                                                @RequestParam("sort") String field,
                                                                @RequestParam("order") String order,
                                                                @RequestHeader(AUTHORIZATION) String token) {
        final String email = jwtTokenUtil.getEmailFromToken(token.substring(7));
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(order), field);
        Page<Comment> commentPage = commentService.findAllCommentsFromArticle(email, articleId, userId, pageable);
        return new ResponseEntity<>(commentPage, HttpStatus.OK);
    }

    @GetMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentFromArticle(@PathVariable Integer articleId,
                                                         @PathVariable Integer commentId,
                                                         @RequestHeader(AUTHORIZATION) String token) {
        final String email = jwtTokenUtil.getEmailFromToken(token.substring(7));
        Comment comment = commentService.findComment(email, articleId, commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<?> deleteCommentFromArticle(@PathVariable Integer articleId,
                                                      @PathVariable Integer commentId,
                                                      @RequestHeader(AUTHORIZATION) String token) {
        final String email = jwtTokenUtil.getEmailFromToken(token.substring(7));
        commentService.deleteComment(email, articleId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
