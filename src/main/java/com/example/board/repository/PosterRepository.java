package com.example.board.repository;

import com.example.board.domain.Poster;

import java.util.List;
import java.util.Optional;

public interface PosterRepository {

    Poster save(Poster poster);
    Optional<Poster> findById(Long id);
    Optional<Poster> findByTitle(String title);
    List<Poster> findAll();
    void deleteById(Long id);

    void edit(Long id, Poster newPoster);
}
