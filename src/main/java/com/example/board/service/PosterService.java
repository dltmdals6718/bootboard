package com.example.board.service;

import com.example.board.domain.Poster;
import com.example.board.repository.PosterRepository;
import com.example.board.repository.SpringDataJpaPosterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class PosterService {
    private SpringDataJpaPosterRepository posterRepository;

    @Autowired
    public PosterService(SpringDataJpaPosterRepository posterRepository) {
        this.posterRepository = posterRepository;
    }


    public Long write(Poster poster) {
        poster.setRegdate(LocalDateTime.now());
        posterRepository.save(poster);
        return poster.getId();
    }

    public List<Poster> findPosters() {
        return posterRepository.findAll();
    }

    public Optional<Poster> findByOne(Long posterId) {
        return posterRepository.findById(posterId);
    }

    public void deletePoster(Long id) {
        posterRepository.deleteById(id);
    }

    public void editPoster(Long id, Poster newPoster) {
        Poster oldPoster = posterRepository.findById(id).get();
        oldPoster.setTitle(newPoster.getTitle());
        oldPoster.setWriter(newPoster.getWriter());
        oldPoster.setContent(newPoster.getContent());
        oldPoster.setRegdate(LocalDateTime.now());
        //posterRepository.edit(id, newPoster);
    }


    public Page<Poster> pageList(Pageable pageable) {
        return posterRepository.findAll(pageable);
    }

}
