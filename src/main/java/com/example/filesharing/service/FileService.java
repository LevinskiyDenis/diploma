package com.example.filesharing.service;

import com.example.filesharing.dto.FileDtoGet;
import com.example.filesharing.dto.FileListDto;
import com.example.filesharing.entity.File;
import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.model.EditNameRequest;
import com.example.filesharing.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@org.springframework.stereotype.Service

public class FileService {

    @Autowired
    UserCredentialsService userCredentialsService;

    @Autowired
    FileRepository fileRepository;

    public Long uploadFile(String filename, MultipartFile multipartFile) throws IOException {

        File file = new File();

        file.setName(filename);
        file.setFile(multipartFile.getBytes());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserCredentials userCredentials = userCredentialsService.loadUserCredentialsByUsername(username);
        file.setUserCredentials(userCredentials);

        return fileRepository.saveAndFlush(file).getId();
    }

    public FileDtoGet getFile(String filename) throws IOException {

        File file = fileRepository.getFileByNameEquals(filename);

//        TODO: код ниже нормально сохраняет файлы, но при передаче через контроллер файлы становятся битыми: сохраняются, но не открываются
//        byte[] data = file.getFile();
//        Path path = Paths.get("/Users/denislevinskiy/Desktop/" + file.getName());
//        Files.write(path, data);

        return new FileDtoGet(file.getFile());
    }

    @Transactional
    public void deleteFile(String filename) {
        fileRepository.deleteByNameEquals(filename);

        // https://stackoverflow.com/questions/32269192/spring-no-entitymanager-with-actual-transaction-available-for-current-thread
    }

    public List<FileListDto> listFiles(int limit) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long usercredentialsId = userCredentialsService.loadUserCredentialsByUsername(username).getId();

        List<File> files = fileRepository.findByUserCredentialsId(usercredentialsId).orElseThrow(() -> new UsernameNotFoundException("Files not found"));

        List<FileListDto> filesDto = new ArrayList<>();

        for (File file : files) {

            FileListDto fileListDto = new FileListDto();
            fileListDto.setFilename(file.getName());
            fileListDto.setSize(file.getFile().length);
            filesDto.add(fileListDto);

        }

        return filesDto;

    }

    public void editFilename(String oldFilename, EditNameRequest editNameRequest) {
        String newFilename = editNameRequest.getNewFilename();
        File file = fileRepository.getFileByNameEquals(oldFilename);
        file.setName(newFilename);
        fileRepository.saveAndFlush(file);
    }
}
