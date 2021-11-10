package com.example.filesharing.service;

import com.example.filesharing.dto.FileDto;
import com.example.filesharing.entity.File;
import com.example.filesharing.model.EditNameRequest;
import com.example.filesharing.repository.FileRepository;
import com.example.filesharing.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;


public class FileServiceUnitTest {

    @InjectMocks
    FileService fileService;

    @Mock
    FileRepository fileRepository;

    @Mock
    FileUtil fileUtil;

    File file;

    @BeforeEach
    void setUp() {
        file = new File();
        file.setId(1L);
        file.setName("myfile");
        openMocks(this);
    }

    @Test
    void uploadFile() throws IOException {

        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        when(fileUtil.createFileFromRequest(any(), any())).thenReturn(file);
        when(fileRepository.saveAndFlush(any())).thenReturn(file);
        when(fileUtil.mapEntityIntoDto(any(), any())).thenReturn(new FileDto());

        fileService.uploadFile(mockMultipartFile.getName(), mockMultipartFile);

        verify(fileUtil, times(1)).createFileFromRequest(any(), any());
        verify(fileRepository, times(1)).saveAndFlush(any());
        verify(fileUtil, times(1)).mapEntityIntoDto(any(), any());
    }

    @Test
    void getFile() throws FileNotFoundException {
        when(fileRepository.getFileByNameEquals(anyString())).thenReturn(Optional.of(file));

        fileService.getFile(file.getName());

        verify(fileRepository, times(1)).getFileByNameEquals(anyString());
    }

    @Test
    void deleteFile() throws FileNotFoundException {
        when(fileRepository.getFileByNameEquals(anyString())).thenReturn(Optional.of(file));

        fileService.deleteFile(file.getName());

        verify(fileRepository, times(1)).getFileByNameEquals(anyString());
        verify(fileRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void editFilename() throws FileNotFoundException {
        when(fileRepository.getFileByNameEquals(anyString())).thenReturn(Optional.of(file));
        when(fileUtil.editFilename(any(), anyString())).thenReturn(file);

        fileService.editFilename("oldname", new EditNameRequest("newname"));

        verify(fileRepository, times(1)).getFileByNameEquals(any());
        verify(fileUtil, times(1)).editFilename(any(), anyString());
        verify(fileRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void listFiles() {
        when(fileUtil.getFileOwnerUserCredentialsId()).thenReturn(1L);
        Page<File> pageFile = new PageImpl<>(List.of(file));
        when(fileRepository.findByUserCredentialsId(anyLong(), any())).thenReturn(pageFile);
        Page<Object> pageObject = new PageImpl<>(List.of(new Object()));
        when(fileUtil.mapEntityPageIntoDtoPage(any(), any())).thenReturn(pageObject);

        fileService.listFiles(Optional.empty(), Optional.empty(), Optional.of(3));

        verify(fileUtil, times(1)).getFileOwnerUserCredentialsId();
        verify(fileRepository, times(1)).findByUserCredentialsId(anyLong(), any());
        verify(fileUtil, times(1)).mapEntityPageIntoDtoPage(any(), any());
    }

}
