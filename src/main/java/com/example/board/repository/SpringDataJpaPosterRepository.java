package com.example.board.repository;

import com.example.board.domain.Category;
import com.example.board.domain.Poster;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaPosterRepository extends JpaRepository<Poster, Long> {
    Page<Poster> findByTitleContaining(String title, Pageable pageable);

    Page<Poster> findByCategoryAndTitleContaining(Category category, String title, Pageable pageable);
    // Page<Poster> findByTitleContainingAndCategory(Category category, String title, Pageable pageable); 순서 주의!
    Page<Poster> findByCategory(Category category, Pageable pageable);
}
