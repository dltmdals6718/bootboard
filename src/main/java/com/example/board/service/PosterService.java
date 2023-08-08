package com.example.board.service;

import com.example.board.domain.Poster;
import com.example.board.domain.UploadFile;
import com.example.board.file.FileStore;
import com.example.board.repository.SpringDataJpaPosterRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class PosterService {
    private SpringDataJpaPosterRepository posterRepository;
    private UploadFileService uploadFileService;
    private FileStore fileStore;


    @Autowired
    public PosterService(SpringDataJpaPosterRepository posterRepository, UploadFileService uploadFileService, FileStore fileStore) {
        this.posterRepository = posterRepository;
        this.uploadFileService = uploadFileService;
        this.fileStore = fileStore;
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

    public void editPoster(Long id, Poster newPoster, List<MultipartFile> files) throws IOException {
        Poster oldPoster = posterRepository.findById(id).get();
        oldPoster.setTitle(newPoster.getTitle());
        oldPoster.setWriter(newPoster.getWriter());
        oldPoster.setContent(newPoster.getContent());
        oldPoster.setRegdate(LocalDateTime.now());

        List<UploadFile> imgFiles = oldPoster.getImgFiles();
        for (UploadFile imgFile : imgFiles) {
            uploadFileService.deleteUploadFile(imgFile);
        }

        List<UploadFile> uploadFiles = fileStore.storeFiles(files);
        uploadFileService.saveAll(uploadFiles);
        oldPoster.setImgFiles(uploadFiles);
    }

    public Page<Poster> pageList(Pageable pageable) {
        return posterRepository.findAll(pageable);
    }

    public Page<Poster> searchPageList(String title, Pageable pageable) {
        return posterRepository.findByTitleContaining(title, pageable);
    }

    public void incrementCommentCnt(Long id) {
        Poster poster = posterRepository.findById(id).get();
        int commentCnt = poster.getCommentCnt();
        poster.setCommentCnt(commentCnt+1);
    }

    public void decreaseCommentCnt(Long id) {
        Poster poster = posterRepository.findById(id).get();
        int commentCnt = poster.getCommentCnt();
        poster.setCommentCnt(commentCnt-1);
    }

}