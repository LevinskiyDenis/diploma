package com.example.filesharing.service;

import com.example.filesharing.dto.FileDto;
import com.example.filesharing.entity.File;
import com.example.filesharing.repository.FileRepository;
import com.example.filesharing.util.FileMapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;

import java.io.FileNotFoundException;
import java.io.IOException;


public class FileServiceUnitTest {

    @InjectMocks
    FileService fileService;

    @Mock
    UserCredentialsService userCredentialsService;

    @Mock
    FileRepository fileRepository;

    @Mock
    FileMapperUtil fileMapperUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void uploadFile() throws IOException {
//
//        File file = new File();
//        file.setName("myfile");
//
//        MockMultipartFile mockMultipartFile
//                = new MockMultipartFile(
//                "file",
//                "hello.txt",
//                MediaType.TEXT_PLAIN_VALUE,
//                "Hello, World!".getBytes()
//        );
//         потому что прайват метод не замокан и все равно вызывается
//        Mockito.when(fileRepository.saveAndFlush(Mockito.any())).thenReturn(file);
//        Mockito.when(fileMapperUtil.mapEntityIntoDto(Mockito.any(), Mockito.any())).thenReturn(new FileDto());
//        fileService.uploadFile(mockMultipartFile.getName(), mockMultipartFile);
//        Mockito.verify(fileRepository, Mockito.times(1)).saveAndFlush(Mockito.any());
//    }

    @Test
    void getFile() throws FileNotFoundException {
        File file = new File();
        file.setName("myfile");

        Mockito.when(fileRepository.getFileByNameEquals(Mockito.anyString())).thenReturn(java.util.Optional.of(file));
        fileService.getFile(file.getName());

        Mockito.verify(fileRepository, Mockito.times(1)).getFileByNameEquals(Mockito.anyString());
    }


}
