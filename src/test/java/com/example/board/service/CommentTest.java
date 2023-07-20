package com.example.board.service;

import com.example.board.domain.Comment;
import com.example.board.repository.SpringDataJpaCommentRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class CommentTest {

    @Autowired
    private SpringDataJpaCommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Test
    @DisplayName("댓글 전송")
    public void commentPost() {
        Comment comment = new Comment();
        comment.setPno(200L);
        comment.setWriter("홍길동");
        comment.setContent("댓글 내용");
        comment.setRegDate(LocalDateTime.now());
        Comment saveComment = commentRepository.save(comment);
        Assertions.assertThat(comment.getWriter()).isEqualTo(saveComment.getWriter());
    }

    @Test
    @DisplayName("해당 게시글 댓글 삭제")
    public void commentDeleteByPno() {
        List<Comment> commentList = commentService.findComments(232L);
        System.out.println("commentList Size = " + commentList.size());
        commentService.deleteCommentByPno(232L);
        Assertions.assertThat(commentService.findComments(232L).size()).isEqualTo(0);
    }


}
