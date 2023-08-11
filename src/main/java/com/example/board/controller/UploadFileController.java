package com.example.board.controller;

import com.example.board.domain.UploadFile;
import com.example.board.file.FileStore;
import com.example.board.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class UploadFileController {

    private UploadFileService uploadFileService;
    private FileStore fileStore;

    @Autowired
    public UploadFileController(UploadFileService uploadFileService, FileStore fileStore) {
        this.uploadFileService = uploadFileService;
        this.fileStore = fileStore;
    }

    @GetMapping("/files/{posterId}")
    public List<UploadFile> findFiles(@PathVariable("posterId") Long pno) throws IOException {
        List<UploadFile> uploadFiles = uploadFileService.findByPno(pno);
        return uploadFiles;
    }
}
