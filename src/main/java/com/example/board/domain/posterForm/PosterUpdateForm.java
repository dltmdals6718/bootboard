package com.example.board.domain.posterForm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
