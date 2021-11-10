package com.example.filesharing.util;

import com.example.filesharing.entity.File;
import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.service.UserCredentialsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class FileUtil {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserCredentialsService userCredentialsService;

    // TODO: FileUtil отдельно, а мапперские методы отдельно в класс MapperUtil

    public <T, D> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        Page<D> map = entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
        return map;
    }

    public <T, D> D mapEntityIntoDto(T t, Class<D> dtoClass) {
        return modelMapper.map(t, dtoClass);
    }

    public File createFileFromRequest(String filename, MultipartFile multipartFile) throws IOException {
        File file = new File();
        file.setName(filename);
        file.setFile(multipartFile.getBytes());
        file.setSize(multipartFile.getBytes().length);
        file.setMimetype(multipartFile.getContentType());
        file.setLastedited(LocalDateTime.now());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserCredentials userCredentials = userCredentialsService.loadUserCredentialsByUsername(username);
        file.setUserCredentials(userCredentials);
        return file;
    }

    public File editFilename(File file, String newFilename) {
        file.setName(newFilename);
        file.setLastedited(LocalDateTime.now());
        return file;
    }

    public Long getFileOwnerUserCredentialsId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userCredentialsService.loadUserCredentialsByUsername(username).getId();
    }

}
