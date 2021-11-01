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

//    public List<FileDtoForList> listFiles(int limit) {
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Long usercredentialsId = userCredentialsService.loadUserCredentialsByUsername(username).getId();
//
//        List<File> files = fileRepository.findByUserCredentialsId(usercredentialsId).orElseThrow(() -> new UsernameNotFoundException("Files not found"));
//
//        List<FileDtoForList> filesDto = new ArrayList<>();
//
//        for (File file : files) {
//
//            FileDtoForList fileDtoForList = new FileDtoForList();
//            fileDtoForList.setFilename(file.getName());
//            fileDtoForList.setSize(file.getFile().length);
//            filesDto.add(fileDtoForList);
//
//        }
//
//        return filesDto;
//
//    }

//    public List<FileDtoForList> listFiles(int limit) {
//
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Long usercredentialsId = userCredentialsService.loadUserCredentialsByUsername(username).getId();
//        List<File> files = fileRepository.findByUserCredentialsId(usercredentialsId).orElseThrow(() -> new UsernameNotFoundException("Files not found"));
//
//        List<FileDtoForList> filesDto = new ArrayList<>();
//
////        for (File file : files) {
////
////            FileDtoForList fileDtoForList = new FileDtoForList();
////            fileDtoForList.setFilename(file.getName());
////            fileDtoForList.setSize(file.getFile().length);
////            filesDto.add(fileDtoForList);
////
////        }
//
//        return filesDto;
//
//    }

    public void editFilename(String oldFilename, EditNameRequest editNameRequest) {
        String newFilename = editNameRequest.getNewFilename();
        File file = fileRepository.getFileByNameEquals(oldFilename);
        file.setName(newFilename);
        fileRepository.saveAndFlush(file);
    }

    public Page<FileDtoForList> listFiles(Optional<String> sort, Optional<Integer> page, Optional<Integer> limit) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long usercredentialsId = userCredentialsService.loadUserCredentialsByUsername(username).getId();

        PageRequest pageRequest = PageRequest.of(page.orElse(0), limit.orElse(10), Sort.Direction.ASC, sort.orElse("id"));

        Page<File> pageFile = fileRepository.findByUserCredentialsId(usercredentialsId, pageRequest);

        // TODO: тут ошибка, маппер не мапит размер файлов и когда последний раз их редактировали,
        // чтобы это исправить, нужно менять саму сущность файла и скрипты ликвибейс
        // добавлять размер файла и когда его последний раз редактировали

        Page<FileDtoForList> pageFileDtoForList = fileMapperUtil.mapEntityPageIntoDtoPage(pageFile, FileDtoForList.class);

        return pageFileDtoForList;
    }
}
