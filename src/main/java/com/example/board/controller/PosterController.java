package com.example.board.controller;

import com.example.board.domain.Comment;
import com.example.board.domain.Poster;
import com.example.board.service.CommentService;
import com.example.board.service.PosterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PosterController {

    private final PosterService posterService;

    private final CommentService commentService;


    @Autowired
    public PosterController(PosterService posterService, CommentService commentService) {
        this.posterService = posterService;
        this.commentService = commentService;
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
        posterService.write(poster);
        return "redirect:/";
    }

    @GetMapping("/posters")
    public String list(Model model, @PageableDefault(sort="id", value=5, direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Poster> pageList = posterService.pageList(pageable);

        model.addAttribute("posters", pageList);
        model.addAttribute("postersSize", pageList.isEmpty());
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());


        model.addAttribute("hasNext", pageList.hasNext());
        model.addAttribute("hasPrevious", pageList.hasPrevious());

        int endPage = (int)(Math.ceil((pageable.getPageNumber()+1)/10.0)*10); // 10, 20, 30, ...
        int startPage = endPage-9; // 10개씩 보여주기 -1 = 9
        if(endPage>pageList.getTotalPages())
            endPage=pageList.getTotalPages();

        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);

        return "posters/posterList";
    }
    @GetMapping("/poster/read")
    public String read(Model model,@RequestParam(name = "id") Long id) {
        Poster poster = posterService.findByOne(id).get();
        model.addAttribute("poster", poster);

        List<Comment> comments = commentService.findComments(id);
        model.addAttribute("comments", comments);
        return "posters/posterView";
    }

    @GetMapping("/poster/delete")
    public String delete(@RequestParam(value="id") Long id) {
        posterService.deletePoster(id);
        return "redirect:/posters";
    }

    @GetMapping("/poster/edit")
    public String editForm(Model model, @RequestParam(value="id") Long id) {
        Poster poster = posterService.findByOne(id).get();
        model.addAttribute(poster);
        return "posters/editPosterForm";
    }

    @PostMapping("/poster/edit")
    public String edit(@RequestParam(value="id") Long id, @Valid Poster poster, Errors errors) {

        if(errors.hasErrors()) {
            return "posters/editPosterForm";
        }
        posterService.editPoster(id, poster);
        return "redirect:/posters";
    }
}