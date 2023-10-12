package com.example.board.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="poster_id")
    private Poster poster;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member writer;
    private String content;

    @Column(name="regdate")
    private LocalDateTime regDate;

    // parent_comment_id, is_parent로 자동 매핑이 될까?
    private Long parentCommentId;
    private boolean parent;

}
