package com.example.board.controller;

import com.example.board.domain.Comment;
import com.example.board.domain.Poster;
import com.example.board.domain.UploadFile;
import com.example.board.file.FileStore;
import com.example.board.repository.UploadFileRepository;
import com.example.board.service.CommentService;
import com.example.board.service.PosterService;
import com.example.board.service.UploadFileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PosterController {

    private final PosterService posterService;

    private final CommentService commentService;

    private FileStore fileStore;

    private UploadFileService uploadFileService;

    @Autowired
    public PosterController(PosterService posterService, CommentService commentService, FileStore fileStore, UploadFileService uploadFileService) {
        this.posterService = posterService;
        this.commentService = commentService;
        this.fileStore = fileStore;
        this.uploadFileService = uploadFileService;
    }

    @GetMapping("/poster/write")
    public String writeForm(Poster poster) {
        return "posters/createPosterForm";
    }

    @PostMapping("/poster/write")
    public String write(@RequestParam(required = false) List<MultipartFile> files, @Valid Poster poster ,Errors errors) throws IOException {

        if(errors.hasErrors()) {
            return "posters/createPosterForm";
        }

        List<UploadFile> uploadFiles = fileStore.storeFiles(files);
        uploadFileService.saveAll(uploadFiles);
        poster.setImgFiles(uploadFiles);
        posterService.write(poster);
        return "redirect:/poster/read?id=" + poster.getId();
    }

    @GetMapping("/posters")
    public String list(Model model, @RequestParam(defaultValue = "0") int page,@RequestParam(required = false, defaultValue = "regdate") String order, @RequestParam(required = false) String searchTitle) {

        Sort sort=Sort.by(Sort.Order.desc("regdate"), Sort.Order.desc("id"));
        if(order!=null&&order.equals("comment"))
            sort = Sort.by(Sort.Order.desc("commentCnt"), Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(page, 5, sort);

        model.addAttribute("page", page);
        model.addAttribute("order", order);
        model.addAttribute("searchTitle", searchTitle);


        Page<Poster> pageList;
        if(searchTitle==null || searchTitle.equals(""))
            pageList = posterService.pageList(pageable);
        else
            pageList = posterService.searchPageList(searchTitle, pageable);

        model.addAttribute("posters", pageList);
        model.addAttribute("postersSize", pageList.isEmpty());
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());


        model.addAttribute("hasNext", pageList.hasNext());
        model.addAttribute("hasPrevious", pageList.hasPrevious());

        int endPage = (int)(Math.ceil((pageable.getPageNumber()+1)/10.0)*10); // 10, 20, 30, ...
        int startPage = endPage-9; // 10개씩 보여주기 -1 = 9
        if(endPage>pageList.getTotalPages())
            endPage=pageList.getTotalPages();

        model.addAttribute("endPage", endPage);
        model.addAttribute("startPage", startPage);

        return "posters/posterList";
    }

    @GetMapping("/poster/read")
    public String read(Model model,@PageableDefault(sort="id", value=5, direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(name = "id") Long id) {
        Poster poster = posterService.findByOne(id).get();
        model.addAttribute("poster", poster);
        List<UploadFile> imgFiles = poster.getImgFiles();
        for(UploadFile file : imgFiles) {
            System.out.println("file = " + file.getUploadFileName());
        }
        return "posters/posterView";
    }

    @GetMapping("/poster/delete")
    public String delete(@RequestParam(value="id") Long id) {
        commentService.deleteCommentByPno(id);
        List<UploadFile> uploadFileList = uploadFileService.findByPno(id);
        for (UploadFile uploadFile : uploadFileList) {
            uploadFileService.deleteUploadFile(uploadFile);
        }
        posterService.deletePoster(id);
        return "redirect:/posters";
    }

    @GetMapping("/poster/edit")
    public String editForm(Model model, @RequestParam(value="id") Long id) {
        Poster poster = posterService.findByOne(id).get();
        model.addAttribute(poster);
        return "posters/editPosterForm";
    }

    @PostMapping("/poster/edit")
    public String edit(@RequestParam(value="id") Long id, @Valid Poster poster, Errors errors) {

        if(errors.hasErrors()) {
            return "posters/editPosterForm";
        }
        posterService.editPoster(id, poster);
        return "redirect:/posters";
    }


    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) throws IOException {
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(filename));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(resource.getFile().toPath()))
                .body(resource);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Long fileId) throws MalformedURLException {

        UploadFile file = uploadFileService.findById(fileId);
        String uploadFileName = file.getUploadFileName();
        String storeFileName = file.getStoreFileName();
        UrlResource urlResource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        String encodeUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodeUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

}