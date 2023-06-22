package com.example.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PosterController {

    @GetMapping("/poster/new")
    public String createForm() {
        return "posters/createPosterForm";
    }
}
