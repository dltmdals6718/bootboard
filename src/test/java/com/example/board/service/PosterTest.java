package com.example.board.service;

import com.example.board.domain.Poster;
import com.example.board.repository.SpringDataJpaPosterRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Transactional
public class PosterTest {

    @Autowired
    PosterService posterService;

    @Autowired
    SpringDataJpaPosterRepository posterRepository;

    @Autowired
    CommentService commentService;

    @Test
    @DisplayName("날짜 최신순 정렬")
    public void posterDatePaging() {
        Sort sort = Sort.by("regdate").descending();
        Pageable pageable = PageRequest.of(0, 5, sort);
        Page<Poster> result = posterRepository.findAll(pageable);
        for (Poster poster : result) {
            System.out.println("poster Id: " + poster.getId());
        }

        pageable = PageRequest.of(1, 5, sort);
        result = posterRepository.findAll(pageable);


        for (Poster poster : result) {
            System.out.println("poster Id: " + poster.getId());
        }
    }

    @Test
    @DisplayName("댓글 개수로 페이징")
    public void commentCntPaging() {
        Sort sort = Sort.by("commentCnt").descending();
        Pageable pageable = PageRequest.of(0, 5, sort);
        Page<Poster> result = posterRepository.findAll(pageable);

        for (Poster poster : result) {
            System.out.println("poster id: "+ poster.getId() );
        }

        pageable = PageRequest.of(1, 5, sort);
        result = posterRepository.findAll(pageable);

        for (Poster poster : result) {
            System.out.println("poster id: "+ poster.getId() );
        }

    }
}
