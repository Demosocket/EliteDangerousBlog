package com.demosocket.blog.controller;

import com.demosocket.blog.model.Comment;
import com.demosocket.blog.dto.CommentNewDto;
import com.demosocket.blog.service.CommentService;
import com.demosocket.blog.security.jwt.JwtTokenUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

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
                                           @RequestBody CommentNewDto commentNewDto,
                                           HttpServletRequest request) {
        final String email = jwtTokenUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));
        commentService.saveNewComment(commentNewDto, email, articleId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<List<Comment>> allCommentsFromArticle(@PathVariable Integer articleId) {
        List<Comment> commentList = commentService.findAllCommentsFromArticle(articleId);
        return new ResponseEntity<>(commentList, HttpStatus.OK);
    }

    @GetMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<Comment> getCommentFromArticle(@PathVariable Integer articleId,
                                                         @PathVariable Integer commentId) {
        Comment comment = commentService.findComment(articleId, commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{articleId}/comments/{commentId}")
    public ResponseEntity<?> deleteCommentFromArticle(@PathVariable Integer articleId,
                                                      @PathVariable Integer commentId,
                                                      HttpServletRequest request) {
        final String email = jwtTokenUtil.getEmailFromToken(request.getHeader("Authorization").substring(7));
        commentService.deleteComment(email, articleId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
