package com.example.board.controller;

import com.example.board.domain.Comment;
import com.example.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    private final CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/write")
    public String commentWrite(Comment comment) {
        commentService.write(comment);
        return "redirect:/poster/read?id=" + comment.getPno();
    }

}
