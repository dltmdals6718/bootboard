package com.example.board.file;

import com.example.board.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {

        List<UploadFile> storeResult = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles) {

            if(multipartFile.getSize() > 0) {

                String uploadFileName = multipartFile.getOriginalFilename();

                String uuid = UUID.randomUUID().toString();
                int pos = uploadFileName.indexOf(".");
                String ext = uploadFileName.substring(pos + 1);

                String storeFileName = uuid + "." + ext;

                multipartFile.transferTo(new File(getFullPath(storeFileName)));
                storeResult.add(new UploadFile(uploadFileName, storeFileName));
            }
        }
        return storeResult;
    }
}
