package com.example.board.controller;

import com.example.board.domain.Comment;
import com.example.board.domain.Poster;
import com.example.board.service.CommentService;
import com.example.board.service.PosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final PosterService posterService;


    @Autowired
    public CommentController(CommentService commentService, PosterService posterService) {
        this.commentService = commentService;
        this.posterService = posterService;
    }

    @PostMapping("/comment/write")
    public String commentWrite(Comment comment) {
        posterService.incrementCommentCnt(comment.getPno());
        commentService.write(comment);
        return "redirect:/poster/read?id=" + comment.getPno();
    }

    @PostMapping("/comment/delete")
    public String commentDelete(Long id) {
        Comment comment = commentService.findComment(id);
        posterService.decreaseCommentCnt(comment.getPno());
        commentService.deleteComment(id);
        return "redirect:/poster/read?id=" + comment.getPno();
    }

}
