package com.example.board.controller;

import com.example.board.domain.Poster;
import com.example.board.service.PosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PosterController {

    private final PosterService posterService;

    public PosterController(PosterService posterService) {
        this.posterService = posterService;
    }


    @GetMapping("/poster/write")
    public String writeForm() {
        return "posters/createPosterForm";
    }

    @PostMapping("/poster/write")
    public String write(Poster poster) {
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
    public String read(Model model,@RequestParam(name = "id", required=true, defaultValue="1") Long id) {
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