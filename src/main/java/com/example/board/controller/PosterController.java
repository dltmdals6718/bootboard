package com.example.board.controller;

import com.example.board.domain.Poster;
import com.example.board.service.PosterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @Autowired
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
        posterService.write(poster);
        return "redirect:/";
    }
    @GetMapping("/posters")
    public String list(Model model, @PageableDefault(sort="id", value=5, direction = Sort.Direction.DESC) Pageable pageable) {

        model.addAttribute("posters", posterService.pageList(pageable));

        Page<Poster> pageList = posterService.pageList(pageable);

        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());


        model.addAttribute("hasNext", pageList.hasNext());
        model.addAttribute("hasPrevious", pageList.hasPrevious());

        int endPage = (int)(Math.ceil((pageable.getPageNumber()+1)/10.0)*10);
        int startPage = endPage-9; // 10개씩 보여주기 -1 = 9
        int realEndPage = pageList.getTotalPages();
        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("realEndPage", realEndPage);

        System.out.println(pageList.getTotalPages()); // 총 페이지
        System.out.println(pageList.getTotalElements()); // 총 게시글
        System.out.println(pageList.getSize()); // 보여줄 게시글 크기

        System.out.println(pageable.getPageSize()); // 보여줄 게시글 크기와 동일 (value)
        System.out.println(pageable.getOffset()); // 시작 게시글 번호
        System.out.println(pageable.getPageNumber()); // 현재 페이지 넘버
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
        // Post로 넘어온 poster에는 제목, 작성자, 내용만 있지 PK인 id는 함께 넘어오지 않아 poster.getId는 null을 가짐.
        //posterService.editPoster(poster.getId(), poster);
        posterService.editPoster(id, poster);
        return "redirect:/posters";
    }
}