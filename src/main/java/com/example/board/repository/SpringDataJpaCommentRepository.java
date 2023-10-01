package com.example.board.repository;

import com.example.board.domain.Comment;
import com.example.board.domain.Poster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaCommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPoster(Poster poster, Pageable pageable); // 페이징된 댓글들
    List<Comment> findByPoster(Poster poster); // pno의 댓글들
    Page<Comment> findByPosterAndParent(Poster poster, boolean isParent, Pageable pageable);
    List<Comment> findByParentCommentIdAndParent(Long parentCommentId, boolean isParent,Pageable pageable);
    Long countByParentCommentIdAndParent(Long parentCommentId, boolean isParent);

    List<Comment> findByParentCommentIdAndParent(Long parentCommentId, boolean isParent);
}
