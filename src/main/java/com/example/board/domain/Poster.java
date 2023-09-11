package com.example.board.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

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

    @NotBlank
    private String title;

    @NotBlank
    private String writer;

    @NotBlank
    private String content;
    private LocalDateTime regdate;

    // BindingResult 적용해보기위해 키, 몸무게 필드 추가
    @NotNull
    @Range(min = 100, max = 250)
    private Long height;

    @NotNull
    @Range(min=50, max = 200)
    private Long weight;

    @JsonBackReference
    @OneToMany(mappedBy = "poster", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UploadFile> imgFiles = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Category category;

    private Boolean fix;

    @Column(name="comment_cnt")
    private int commentCnt;

}
