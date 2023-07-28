package com.example.board.repository;

import com.example.board.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaCommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPno(Long pno, Pageable pageable); // 페이징된 댓글들
    List<Comment> findByPno(Long pno); // pno의 댓글들
    Page<Comment> findByPnoAndIsParent(Long pno, boolean isParent, Pageable pageable);
    List<Comment> findByParentCommentIdAndIsParent(Long parentCommentId, boolean isParent,Pageable pageable);
    Long countByParentCommentIdAndIsParent(Long parentCommentId, boolean isParent);
}
