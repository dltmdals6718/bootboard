package com.example.board.service;

import com.example.board.domain.Poster;
import com.example.board.repository.PosterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PosterService {
    private PosterRepository posterRepository;

    @Autowired
    public PosterService(PosterRepository posterRepository) {
        this.posterRepository = posterRepository;
    }

    @Transactional
    public Long write(Poster poster) {
        posterRepository.save(poster);
        return poster.getId();
    }

    public List<Poster> findPosters() {
        return posterRepository.findAll();
    }

    public Optional<Poster> findByOne(Long posterId) {
        return posterRepository.findById(posterId);
    }

}
