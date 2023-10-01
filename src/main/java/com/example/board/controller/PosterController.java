package com.example.board.controller;

import com.example.board.domain.*;
import com.example.board.domain.posterForm.PosterSaveForm;
import com.example.board.domain.posterForm.PosterUpdateForm;
import com.example.board.file.FileStore;
import com.example.board.service.CommentService;
import com.example.board.service.PosterService;
import com.example.board.service.UploadFileService;
import com.example.board.validation.PosterValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.validation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PosterController {

    private final PosterService posterService;
    private final CommentService commentService;
    private final FileStore fileStore;
    private final UploadFileService uploadFileService;
    private final PosterValidation posterValidation;


    @InitBinder("poster")
    public void init(WebDataBinder webDataBinder) {
        //거log.info("init binder = {}", webDataBinder);
        //webDataBinder.addValidators(posterValidation);
    }

    @GetMapping("/posters/{category}/write")
    public String writeForm(@PathVariable Category category, Model model) {
        model.addAttribute("category", category);
        model.addAttribute("poster", new Poster());
        return "posters/createPosterForm";
    }

    @PostMapping("/posters/{category}")
    public String write(@PathVariable("category") Category category,
                        @RequestParam(required = false) List<MultipartFile> files,
                        @SessionAttribute(name=SessionConst.LOGIN_MEMBER) Member loginMember,
                        @Validated @ModelAttribute(name = "poster") PosterSaveForm form, BindingResult bindingResult) throws IOException {

        if(bindingResult.hasErrors()) {
            System.out.println("bindingResult = " + bindingResult);
            return "posters/createPosterForm";
        }

        Poster poster = new Poster();
        poster.setCategory(category);
        poster.setTitle(form.getTitle());
        poster.setWriter(loginMember);
        poster.setContent(form.getContent());
        poster.setHeight(form.getHeight());
        poster.setWeight(form.getWeight());
        poster.setFix(form.getFix());

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
    public String list(@PathVariable("category") Category category, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "regdate") String order, @RequestParam(required = false) String searchTitle) {

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
        Poster poster = posterService.findByOne(id).get();
        Category category = poster.getCategory();
        commentService.deleteCommentByPoster(poster);
        posterService.deletePoster(id);
        return "redirect:/posters/" + category;
    }

    @GetMapping("/poster/edit")
    public String editForm(Model model, @RequestParam(value="id") Long id) {
        Poster poster = posterService.findByOne(id).get();
        model.addAttribute("category", poster.getCategory());
        model.addAttribute("poster", poster);
        return "posters/editPosterForm";
    }

    @PostMapping("/poster/edit")
    public String edit(@RequestParam(value="id") Long id,
                       @RequestParam(required = false) List<MultipartFile> files,
                       @RequestParam(required = false) List<Long> deleteFilesId,
                       @SessionAttribute(name= SessionConst.LOGIN_MEMBER) Member loginMember,
                       @Validated @ModelAttribute("poster") PosterUpdateForm form, Errors errors, Model model) throws IOException {
        Category category = posterService.findByOne(id).get().getCategory();

        if(errors.hasErrors()) { // 수정 필요 editPosterForm 넘어갈땐 poster의 category
            System.out.println("errors = " + errors);
            model.addAttribute("category", category);
            return "posters/editPosterForm";
        }

        Poster poster = new Poster();
        poster.setTitle(form.getTitle());
        poster.setWriter(loginMember); // 세션 정보로 작성자 변경
        poster.setContent(form.getContent());
        poster.setHeight(form.getHeight());
        poster.setWeight(form.getWeight());
        poster.setFix(form.getFix());

        posterService.editPoster(id, poster, files, deleteFilesId);
        return "redirect:/posters/view/" + id;
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