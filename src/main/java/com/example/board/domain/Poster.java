package com.example.board.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

    @OneToMany
    @JoinColumn(name="pno")
    private List<UploadFile> imgFiles;

    @Column(name="comment_cnt")
    private int commentCnt;

}
