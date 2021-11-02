package com.example.filesharing.service;

import com.example.filesharing.dto.FileDtoGet;
import com.example.filesharing.dto.FileDtoForList;
import com.example.filesharing.entity.File;
import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.model.EditNameRequest;
import com.example.filesharing.repository.FileRepository;
import com.example.filesharing.util.FileMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


@org.springframework.stereotype.Service

public class FileService {

    @Autowired
    UserCredentialsService userCredentialsService;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    FileMapperUtil fileMapperUtil;

    public Long uploadFile(String filename, MultipartFile multipartFile) throws IOException {

        File file = new File();

        file.setName(filename);
        file.setFile(multipartFile.getBytes());
        file.setSize(multipartFile.getBytes().length);
        file.setMimetype(multipartFile.getContentType());
        file.setLastedited(LocalDateTime.now());

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

        return fileMapperUtil.mapEntityIntoDto(file, FileDtoGet.class);
    }

    @Transactional
    public void deleteFile(String filename) {
        fileRepository.deleteByNameEquals(filename);

        // https://stackoverflow.com/questions/32269192/spring-no-entitymanager-with-actual-transaction-available-for-current-thread
    }

    public void editFilename(String oldFilename, EditNameRequest editNameRequest) {
        String newFilename = editNameRequest.getNewFilename();
        File file = fileRepository.getFileByNameEquals(oldFilename);
        file.setName(newFilename);
        file.setLastedited(LocalDateTime.now());
        fileRepository.saveAndFlush(file);
    }

    public Page<FileDtoForList> listFiles(Optional<String> sort, Optional<Integer> page, Optional<Integer> limit) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long usercredentialsId = userCredentialsService.loadUserCredentialsByUsername(username).getId();

        PageRequest pageRequest = PageRequest.of(page.orElse(0), limit.orElse(10), Sort.Direction.ASC, sort.orElse("id"));

        Page<File> pageFile = fileRepository.findByUserCredentialsId(usercredentialsId, pageRequest);

        Page<FileDtoForList> pageFileDtoForList = fileMapperUtil.mapEntityPageIntoDtoPage(pageFile, FileDtoForList.class);

        return pageFileDtoForList;
    }
}
