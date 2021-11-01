package com.example.filesharing.controller;

import com.example.filesharing.dto.FileDtoGet;
import com.example.filesharing.dto.FileDtoForList;
import com.example.filesharing.model.EditNameRequest;
import com.example.filesharing.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")

public class FileController {

    FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(path = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Long uploadFile(@RequestParam String filename, @RequestParam MultipartFile file) throws IOException {
        return fileService.uploadFile(filename, file);
    }

    // TODO: возвращает битые файлы
    @GetMapping(path = "file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultiValueMap<String, Object>> getFile(@RequestParam String filename) throws IOException {

//      Способ 1: возвращает multipart с content-type: application/json

        FileDtoGet fileDtoGet = fileService.getFile(filename);
        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
        formData.add("fileDtoGet", fileDtoGet);
        return ResponseEntity.ok(formData);

//        Способ 2: возвращает multipart с хардкодным content-type: image/png

//        FileDtoGet fileDtoGet = fileService.getFile(filename);
//        MultiValueMap<String, Object> formData = new LinkedMultiValueMap<String, Object>();
//        ByteArrayResource byteArrayResource = new ByteArrayResource(fileDtoGet.getFile());
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        HttpEntity<Resource> entity = new HttpEntity<Resource>(byteArrayResource, headers);
//        formData.add("fileDtoGet", entity);
//        return ResponseEntity.ok(formData);

//        Способ 3: возвращает attachment с хардкодным content-type: image/png

//        FileDtoGet fileDtoGet = fileService.getFile(filename);
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(MediaType.IMAGE_PNG_VALUE))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "name" + "\"")
//                .body(new ByteArrayResource(fileDtoGet.getFile()));

    }

    @PutMapping(path = "file", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void editFilename(@RequestParam("filename") String oldFilename, @RequestBody EditNameRequest editNameRequest) {
        fileService.editFilename(oldFilename, editNameRequest);
    }

    @DeleteMapping(path = "file")
    public void deleteFile(@RequestParam String filename) {
        fileService.deleteFile(filename);
    }

//    @GetMapping(path = "/list")
//    public List<FileDtoForList> listFiles(@RequestParam int limit) {
//        return fileService.listFiles(limit);
//    }

    @GetMapping(path = "/list")
    public Page<FileDtoForList> listFiles(@RequestParam Optional<String> sort,
                                          @RequestParam Optional<Integer> page,
                                          @RequestParam Optional<Integer> limit) {
        return fileService.listFiles(sort, page, limit);
    }




}
