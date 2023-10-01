package com.example.board.service;


import com.example.board.domain.Comment;
import com.example.board.repository.SpringDataJpaCommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommentService {

    private SpringDataJpaCommentRepository commentRepository;
    private PosterService posterService;

    @Autowired
    public CommentService(SpringDataJpaCommentRepository commentRepository, PosterService posterService) {
        this.commentRepository = commentRepository;
        this.posterService = posterService;
    }

    public Long write(Comment comment) {
        comment.setRegDate(LocalDateTime.now());
        if(comment.isParent()) {
            posterService.incrementCommentCnt(comment.getPno());
        }

        commentRepository.save(comment); // DB에 저장할때까지 id를 알 수 없으므로

        if(comment.isParent())
            comment.setParentCommentId(comment.getId());

        return comment.getId();
    }

    public void deleteCommentByPno(Long pno) {
        List<Comment> commentList = commentRepository.findByPno(pno);
        for (Comment comment : commentList) {
            commentRepository.delete(comment);
        }
    }
    public Page<Comment> findPagingComments(Long pno, boolean isParent ,Pageable pageable) {
        Page<Comment> comments = commentRepository.findByPnoAndParent(pno, isParent ,pageable);
        return comments;
    }

    public List<Comment> findComments(Long pno) {
        return commentRepository.findByPno(pno);
    }

    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).get();
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
    public Map<String, Object> findReply(Long parentCommentId, int page) {
        Sort sort=Sort.by(Sort.Order.desc("regDate"), Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 5, sort);
        Map<String, Object> m = new HashMap<>();
        m.put("content" ,commentRepository.findByParentCommentIdAndParent(parentCommentId, false ,pageable));
        m.put("totalSize", commentRepository.countByParentCommentIdAndParent(parentCommentId, false));
        return m;
    }

    public List<Comment> findByParentCommentIdAndIsParent(Long parentCommentId, boolean isParent) {
        return commentRepository.findByParentCommentIdAndParent(parentCommentId, isParent);
    }

}
