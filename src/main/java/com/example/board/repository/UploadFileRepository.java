package com.example.board.repository;

import com.example.board.domain.Poster;
import com.example.board.domain.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
    //List<UploadFile> findByPno(Long pno);
    List<UploadFile> findByPoster(Poster poster);
}
