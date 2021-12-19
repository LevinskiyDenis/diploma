package com.example.filesharing.service;

import com.example.filesharing.dto.FileDto;
import com.example.filesharing.entity.File;
import com.example.filesharing.model.EditNameRequest;
import com.example.filesharing.repository.FileRepository;
import com.example.filesharing.util.FileUtil;
import com.example.filesharing.util.MapperUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;


@org.springframework.stereotype.Service

public class FileService {

    private final FileRepository fileRepository;
    private final FileUtil fileUtil;
    private final MapperUtil mapperUtil;

    public FileService(FileRepository fileRepository, FileUtil fileUtil, MapperUtil mapperUtil) {
        this.fileRepository = fileRepository;
        this.fileUtil = fileUtil;
        this.mapperUtil = mapperUtil;
    }

    public FileDto uploadFile(String filename, MultipartFile multipartFile) throws IOException {
        File file = fileUtil.createFileFromRequest(filename, multipartFile);

        File savedFile = fileRepository.saveAndFlush(file);

        return mapperUtil.mapEntityIntoDto(savedFile, FileDto.class);
    }

    public File getFile(String filename) throws FileNotFoundException {
        return fileRepository.findFileByNameEquals(fileUtil.getFileOwnerUserCredentialsId(), filename).orElseThrow(FileNotFoundException::new);
    }

    public void deleteFile(String filename) throws FileNotFoundException {
        File file = fileRepository.findFileByNameEquals(fileUtil.getFileOwnerUserCredentialsId(), filename).orElseThrow(FileNotFoundException::new);
        fileRepository.deleteById(file.getId());
    }

    public void editFilename(String oldFilename, EditNameRequest editNameRequest) throws FileNotFoundException {
        File file = fileRepository.findFileByNameEquals(fileUtil.getFileOwnerUserCredentialsId(), oldFilename).orElseThrow(FileNotFoundException::new);
        File withNewFilename = fileUtil.editFilename(file, editNameRequest.getNewFilename());
        fileRepository.saveAndFlush(withNewFilename);
    }

    public Page<FileDto> listFiles(Optional<String> sort, Optional<Integer> page, Optional<Integer> limit) {
        Long userCredentialsId = fileUtil.getFileOwnerUserCredentialsId();
        PageRequest pageRequest = PageRequest.of(page.orElse(0), limit.orElse(10), Sort.Direction.ASC, sort.orElse("id"));
        Page<File> pageFile = fileRepository.findFilesByUserCredentialsId(userCredentialsId, pageRequest);
        return mapperUtil.mapEntityPageIntoDtoPage(pageFile, FileDto.class);
    }

}
