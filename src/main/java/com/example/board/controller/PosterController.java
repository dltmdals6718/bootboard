package com.example.board.controller;

import com.example.board.domain.Category;
import com.example.board.domain.Poster;
import com.example.board.domain.UploadFile;
import com.example.board.file.FileStore;
import com.example.board.service.CommentService;
import com.example.board.service.PosterService;
import com.example.board.service.UploadFileService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    @GetMapping("/posters/{category}/write")
    public String writeForm(@PathVariable Category category, Model model) {
        model.addAttribute("category", category);
        model.addAttribute("poster", new Poster());
        return "posters/createPosterForm";
    }

    @PostMapping("/posters/{category}")
    public String write(@PathVariable("category") Category category, @RequestParam(required = false) List<MultipartFile> files, @ModelAttribute Poster poster , BindingResult bindingResult) throws IOException {

        if(poster.getHeight()!=null && poster.getWeight()!=null) {
            Long height = poster.getHeight();
            Long weight = poster.getWeight();

            if(height+weight<150) {
                //bindingResult.addError(new ObjectError("poster", "키와 몸무게의 합은 150이상이여야 합니다."));
                bindingResult.addError(new ObjectError("poster", null, null, "키와 몸무게의 합은 150이상이여야 합니다."));
            }
        }

        // input의 type=text는 아무것도 입력하지 않아도 null이 되진 않음을 주의.
        if(poster.getTitle().equals("")) {
            bindingResult.addError(new FieldError("poster", "title", "제목을 입력하세요."));
        }

        if(poster.getWriter().equals("")) {
            bindingResult.addError(new FieldError("poster", "writer", "작성자를 입력하세요."));
        }

        if(poster.getContent().equals("")) {
            bindingResult.addError(new FieldError("poster", "content", "내용을 입력하세요."));
        }

        if(poster.getHeight()==null||poster.getHeight()<100) {
            //bindingResult.addError(new FieldError("poster", "height", "키는 100이상이어야 합니다."));
            // 100이상이 아니면 입력 값이 초기화된다. 이것을 아래의 메서드로 해결.
            bindingResult.addError(new FieldError("poster", "height", poster.getHeight(), false, null, null, "키는 100이상이어야 합니다."));
        }

        if(poster.getWeight()==null||poster.getWeight()<40) {
            bindingResult.addError(new FieldError("poster", "weight", "몸무게는 40이상이어야 합니다."));
            //bindingResult.addError(new FieldError("poster", "weight", poster.getWeight(), false, null, null, "몸무게는 40이상이어야 합니다."));
        }

        if(bindingResult.hasErrors()) {
            return "posters/createPosterForm";
        }

        poster.setCategory(category);
        if(files!=null) {
            List<UploadFile> uploadFiles = fileStore.storeFiles(files);
            //uploadFileService.saveAll(uploadFiles); cascade 작성으로인해 불필요한 코드
            for (UploadFile uploadFile : uploadFiles) {
                uploadFile.setPoster(poster);
            }
            poster.setImgFiles(uploadFiles);
        }
        posterService.write(poster);
        return "redirect:/posters/view/"+poster.getId();
    }

    @GetMapping("/posters/{category}")
    public String list(@PathVariable("category") Category category, Model model, @RequestParam(defaultValue = "0") int page,@RequestParam(required = false, defaultValue = "regdate") String order, @RequestParam(required = false) String searchTitle) {

        Sort sort=Sort.by(Sort.Order.desc("regdate"), Sort.Order.desc("id"));
        if(order!=null&&order.equals("comment"))
            sort = Sort.by(Sort.Order.desc("commentCnt"), Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(page, 5, sort);

        model.addAttribute("page", page);
        model.addAttribute("order", order);
        model.addAttribute("searchTitle", searchTitle);


        Page<Poster> pageList;
        if(searchTitle==null || searchTitle.equals(""))
            pageList = posterService.pageList(category, pageable);
        else
            pageList = posterService.searchPageList(category, searchTitle, pageable);

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

        model.addAttribute("category", category);

        List<Poster> fixedPoster = posterService.fixPosterList(category, true);
        model.addAttribute("fixedPosters", fixedPoster);
        return "posters/posterList";
    }

    @GetMapping("/posters/view/{id}")
    public String read(@PathVariable Long id, Model model) {
        Poster poster = posterService.findByOne(id).get();
        model.addAttribute("poster", poster);
        List<UploadFile> imgFiles = poster.getImgFiles();
        for(UploadFile file : imgFiles) {
            System.out.println("file = " + file.getUploadFileName());
        }
        model.addAttribute("category", poster.getCategory());
        return "posters/posterView";
    }

    @GetMapping("/poster/delete")
    public String delete(@RequestParam(value="id") Long id) {
        Category category = posterService.findByOne(id).get().getCategory();
        commentService.deleteCommentByPno(id);
        posterService.deletePoster(id);
        return "redirect:/posters/" + category;
    }

    @GetMapping("/poster/edit")
    public String editForm(Model model, @RequestParam(value="id") Long id) {
        Poster poster = posterService.findByOne(id).get();
        model.addAttribute("poster", poster);
        return "posters/editPosterForm";
    }

    @PostMapping("/poster/edit")
    public String edit(@RequestParam(value="id") Long id,
                       @RequestParam(required = false) List<MultipartFile> files,
                       @RequestParam(required = false) List<Long> deleteFilesId,
                       @Valid Poster poster, Errors errors) throws IOException {

        if(errors.hasErrors()) { // 수정 필요
            return "posters/editPosterForm";
        }
        Category category = posterService.findByOne(id).get().getCategory();
        posterService.editPoster(id, poster, files, deleteFilesId);
        return "redirect:/posters/" + category;
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