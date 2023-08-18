package com.example.board.service;

import com.example.board.domain.UploadFile;
import com.example.board.file.FileStore;
import com.example.board.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class UploadFileService {

    private UploadFileRepository uploadFileRepository;
    private FileStore fileStore;


    @Autowired
    public UploadFileService(UploadFileRepository uploadFileRepository, FileStore fileStore) {
        this.uploadFileRepository = uploadFileRepository;
        this.fileStore = fileStore;
    }

    public void saveAll(List<UploadFile> iter) {
        uploadFileRepository.saveAll(iter);
    }

    public List<UploadFile> findByPno(Long pno) {
        return uploadFileRepository.findByPno(pno);
    }

    public void deleteUploadFile(UploadFile uploadFile) {
        String fullPath = fileStore.getFullPath(uploadFile.getStoreFileName());
        File file = new File(fullPath);
        if(file.exists())
            file.delete();
    }

    public void deleteByIds(List<Long> deleteFileIds) {

        List<UploadFile> deleteFiles = uploadFileRepository.findAllById(deleteFileIds);
        for (UploadFile deleteFile : deleteFiles) {
            uploadFileRepository.deleteById(deleteFile.getId()); // DB 삭제
            deleteUploadFile(deleteFile); // 파일 삭제
        }

    }
    public UploadFile findById(Long id) {
        return uploadFileRepository.findById(id).get();
    }
}
