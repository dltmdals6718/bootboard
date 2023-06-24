package com.example.board.repository;

import com.example.board.domain.Poster;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class JpaPosterRepository implements PosterRepository{

    private final EntityManager em;

    @Autowired
    public JpaPosterRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Poster save(Poster poster) {
        em.persist(poster);
        return poster;
    }

    @Override
    public Optional<Poster> findById(Long id) {
        Poster poster = em.find(Poster.class, id);
        return Optional.ofNullable(poster);
    }

    @Override
    public Optional<Poster> findByTitle(String title) {
        List<Poster> posters = em.createQuery("SELECT p FROM Poster p WHERE p.title = :title2", Poster.class)
                .setParameter("title2", title)
                .getResultList();

        return posters.stream().findAny();
    }

    @Override
    public List<Poster> findAll() {
        return em.createQuery("SELECT p FROM Poster as p", Poster.class)
                .getResultList();
    }
}
