package com.example.board.controller;

import com.example.board.domain.Comment;
import com.example.board.domain.Poster;
import com.example.board.service.CommentService;
import com.example.board.service.PosterService;
import lombok.RequiredArgsConstructor;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PosterService posterService;


    @PostMapping(value = "/comment/write", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Comment commentWrite(@RequestBody Comment comment, @RequestParam Long pno) {
        Poster poster = posterService.findByOne(pno).get();
        comment.setPoster(poster);
        commentService.write(comment);
        return comment;
    }

    @PostMapping(value = "/comment/write", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String commentWrite2(@ModelAttribute Comment comment) {
        posterService.incrementCommentCnt(comment.getPoster().getId());
        commentService.write(comment);
        return "redirect:/poster/read?id="+comment.getPoster().getId();
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
        if(comment.isParent()) {
            posterService.decreaseCommentCnt(comment.getPoster().getId());
            List<Comment> commentList = commentService.findByParentCommentIdAndIsParent(id, false);
            for(Comment childComment : commentList) {
                commentService.deleteComment(childComment.getId());
            }
        }
        commentService.deleteComment(id);
        return comment;
    }
    @GetMapping("/reply")
    @ResponseBody
    public Map<String, Object> findReply(Long parentCommentId, int page) {
        return commentService.findReply(parentCommentId, page);
    }

}
