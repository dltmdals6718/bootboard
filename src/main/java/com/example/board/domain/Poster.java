package com.example.board.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

public class Poster {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "작성자를 입력하세요.")
    private String writer;


    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    private LocalDateTime regdate;

    @JsonBackReference
    @OneToMany(mappedBy = "poster", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UploadFile> imgFiles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    private Boolean fix;

    @Column(name="comment_cnt")
    private int commentCnt;

}
