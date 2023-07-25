package com.example.board.controller;

import com.example.board.domain.Comment;
import com.example.board.domain.Poster;
import com.example.board.service.CommentService;
import com.example.board.service.PosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final PosterService posterService;


    @Autowired
    public CommentController(CommentService commentService, PosterService posterService) {
        this.commentService = commentService;
        this.posterService = posterService;
    }

    @PostMapping(value = "/comment/write", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Comment commentWrite(@RequestBody Comment comment) {
        posterService.incrementCommentCnt(comment.getPno());
        commentService.write(comment);
        return comment;
    }

    @PostMapping(value = "/comment/write", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String commentWrite2(@ModelAttribute Comment comment) {
        posterService.incrementCommentCnt(comment.getPno());
        commentService.write(comment);
        return "redirect:/poster/read?id="+comment.getPno();
    }
    @GetMapping("/comments")
    @ResponseBody
    public Page<Comment> commentList(@PageableDefault(sort="id", value=10, direction = Sort.Direction.DESC) Pageable pageable, @RequestParam(name = "pno") Long pno) {
        Page<Comment> comments = commentService.findPagingComments(pno, pageable);
        return comments;
    }

    @PostMapping("/comment/delete")
    public void commentDelete(Long id) {
        Comment comment = commentService.findComment(id);
        posterService.decreaseCommentCnt(comment.getPno());
        commentService.deleteComment(id);
    }

}
