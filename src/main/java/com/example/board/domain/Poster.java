package com.example.board.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Poster {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name="writer")
    private Member writer;

    private String content;
    private LocalDateTime regdate;

    // BindingResult 적용해보기위해 키, 몸무게 필드 추가
    private Long height;

    private Long weight;

    @JsonManagedReference
    @OneToMany(mappedBy = "poster", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UploadFile> imgFiles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    private Boolean fix;

    @Column(name="comment_cnt")
    private int commentCnt;

}
