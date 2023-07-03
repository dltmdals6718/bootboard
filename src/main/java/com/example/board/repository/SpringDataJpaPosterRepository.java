package com.example.board.repository;

import com.example.board.domain.Poster;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaPosterRepository extends JpaRepository<Poster, Long>, PosterRepository {

}
