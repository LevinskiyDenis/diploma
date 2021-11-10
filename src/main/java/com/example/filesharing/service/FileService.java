package com.example.filesharing.service;

import com.example.filesharing.dto.FileDto;
import com.example.filesharing.entity.File;
import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.model.EditNameRequest;
import com.example.filesharing.repository.FileRepository;
import com.example.filesharing.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;


@org.springframework.stereotype.Service

public class FileService {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    FileUtil fileUtil;

    public FileDto uploadFile(String filename, MultipartFile multipartFile) throws IOException {

        File file = fileUtil.createFileFromRequest(filename, multipartFile);

        File savedFile = fileRepository.saveAndFlush(file);

        return fileUtil.mapEntityIntoDto(savedFile, FileDto.class);
    }

    public File getFile(String filename) throws FileNotFoundException {
        return fileRepository.getFileByNameEquals(filename).orElseThrow(FileNotFoundException::new);
    }

    @Transactional
    public void deleteFile(String filename) throws FileNotFoundException {

        File file = fileRepository.getFileByNameEquals(filename).orElseThrow(FileNotFoundException::new);
        fileRepository.deleteById(file.getId());
        // https://stackoverflow.com/questions/32269192/spring-no-entitymanager-with-actual-transaction-available-for-current-thread
    }

    public void editFilename(String oldFilename, EditNameRequest editNameRequest) throws FileNotFoundException {
        File file = fileRepository.getFileByNameEquals(oldFilename).orElseThrow(FileNotFoundException::new);
        File withNewFilename = fileUtil.editFilename(file, editNameRequest.getNewFilename());
        fileRepository.saveAndFlush(withNewFilename);
    }

    public Page<FileDto> listFiles(Optional<String> sort, Optional<Integer> page, Optional<Integer> limit) {

        Long userCredentialsId = fileUtil.getFileOwnerUserCredentialsId();

        PageRequest pageRequest = PageRequest.of(page.orElse(0), limit.orElse(10), Sort.Direction.ASC, sort.orElse("id"));

        Page<File> pageFile = fileRepository.findByUserCredentialsId(userCredentialsId, pageRequest);

        return fileUtil.mapEntityPageIntoDtoPage(pageFile, FileDto.class);
    }

}
