package com.example.board.repository;

import com.example.board.domain.Poster;
import org.springframework.stereotype.Repository;

import java.util.*;


public class MemoryPosterRepository implements PosterRepository{

    private Map<Long, Poster> store = new HashMap<>();
    private Long sequence = 0L;

    @Override
    public Poster save(Poster poster) {
        poster.setId(++sequence);
        store.put(poster.getId(), poster);
        return poster;
    }

    @Override
    public Optional<Poster> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Poster> findByTitle(String title) {
        return store.values().stream()
                .filter(member -> member.getTitle().equals(title))
                .findAny(); // 추후에 중복 제목 불가도 해보고 중복 제목 검색 작업 해보자.
    }

    @Override
    public List<Poster> findAll() {
        return new ArrayList<>(store.values());
    }
}
