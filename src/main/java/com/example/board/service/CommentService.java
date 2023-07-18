package com.example.board.service;


import com.example.board.domain.Comment;
import com.example.board.repository.SpringDataJpaCommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CommentService {

    private SpringDataJpaCommentRepository commentRepository;

    @Autowired
    public CommentService(SpringDataJpaCommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Long write(Comment comment) {
        comment.setRegDate(LocalDateTime.now());
        commentRepository.save(comment);
        return comment.getId();
    }

    public Page<Comment> findComments(Long pno, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPno(pno, pageable);
        return comments;
    }

    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

}
