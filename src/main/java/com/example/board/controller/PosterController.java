package com.example.board.controller;

import com.example.board.domain.Poster;
import com.example.board.service.PosterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PosterController {

    private final PosterService posterService;

    public PosterController(PosterService posterService) {
        this.posterService = posterService;
    }


    @GetMapping("/poster/write")
    public String writeForm(Model model) {
        model.addAttribute("poster", new Poster());
        return "posters/createPosterForm";
    }

    @PostMapping("/poster/write")
    public String write(@Valid Poster poster, Errors errors) {
        if(errors.hasErrors()) {
            return "posters/createPosterForm";
        }

        poster.setRegdate(LocalDateTime.now());
        posterService.write(poster);
        return "redirect:/";
    }

    @GetMapping("/posters")
    public String list(Model model) {
        List<Poster> posters = posterService.findPosters();
        model.addAttribute("posters", posters);
        return "posters/posterList";
    }

    @GetMapping("/poster/read")
    public String read(Model model,@RequestParam(name = "id") Long id) {
        Poster poster = posterService.findByOne(id).get();
        model.addAttribute("poster", poster);
        return "posters/posterView";
    }

    @GetMapping("/poster/delete")
    public String delete(@RequestParam(value="id") Long id) {
        posterService.deletePoster(id);
        return "redirect:/posters";
    }

}