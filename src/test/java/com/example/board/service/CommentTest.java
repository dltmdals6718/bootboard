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

@SpringBootTest
// @Transactional 이거 붙이고 안붙이고 차이도 작성하기.
public class CommentTest {

    @Autowired
    private SpringDataJpaCommentRepository commentRepository;

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


}
