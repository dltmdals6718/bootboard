package com.example.board.domain.posterForm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class PosterSaveForm {

    @NotBlank
    private String title;

    @NotBlank
    private String writer;

    @NotBlank
    private String content;

    @NotNull
    @Range(min = 100, max = 250)
    private Long height;

    @NotNull
    @Range(min=50, max = 200)
    private Long weight;

    @NotNull
    private Boolean fix;
}
