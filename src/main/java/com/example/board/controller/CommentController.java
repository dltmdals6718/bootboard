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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final PosterService posterService;


    @Autowired
    public CommentController(CommentService commentService, PosterService posterService) {
        this.commentService = commentService;
        this.posterService = posterService;
    }

    /* 버그 있음 찾아봐
    @PostMapping(value = "/comment/write", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Comment commentWrite(@RequestBody Comment comment, @RequestParam(required = false) Long parentCommentId) {

        if(parentCommentId == null) {
            posterService.incrementCommentCnt(comment.getPno());
            comment.setParent(true);
            comment.setParentCommentId(comment.getId());
            System.out.println("comment = " + comment);
        } else {
            comment.setParent(false);
            Comment parentComment = commentService.findComment(parentCommentId);
            comment.setParentCommentId(parentComment.getId());
        }

        commentService.write(comment);
        return comment;
    }*/
    @PostMapping(value = "/comment/write", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Comment commentWrite(@RequestBody Comment comment) {
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
        Page<Comment> comments = commentService.findPagingComments(pno, true, pageable);
        return comments;
    }

    @PostMapping("/comment/delete")
    @ResponseBody
    public Comment commentDelete(Long id) {
        Comment comment = commentService.findComment(id);
        if(comment.getIsParent())
            posterService.decreaseCommentCnt(comment.getPno());
        commentService.deleteComment(id);
        return comment;
    }
    @GetMapping("/reply")
    @ResponseBody
    public Map<String, Object> findReply(Long parentCommentId, int page) {
        return commentService.findReply(parentCommentId, page);
    }

}
