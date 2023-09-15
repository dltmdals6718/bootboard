package com.example.board.domain.form;

import com.example.board.domain.Category;
import com.example.board.domain.UploadFile;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PosterUpdateForm {

    @NotBlank
    private String title;

    @NotBlank
    private String writer;

    @NotBlank
    private String content;

    private Long height;

    private Long weight;

    @NotNull
    private Boolean fix;
}
