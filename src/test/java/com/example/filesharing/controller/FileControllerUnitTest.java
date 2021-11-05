package com.example.filesharing.controller;

import com.example.filesharing.dto.FileDto;
import com.example.filesharing.entity.File;
import com.example.filesharing.filters.JwtRequestFilter;
import com.example.filesharing.model.EditNameRequest;
import com.example.filesharing.service.FileService;
import com.example.filesharing.service.UserCredentialsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = FileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FileControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FileService fileService;

    @MockBean
    UserCredentialsService userCredentialsService;

    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @Test
    void uploadFile() throws Exception {

        FileDto fileDto = new FileDto(1L, "file", 10L, MediaType.TEXT_PLAIN_VALUE, LocalDateTime.now());
        Mockito.when(fileService.uploadFile(anyString(), any(MultipartFile.class))).thenReturn(fileDto);

        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/file").file(mockMultipartFile)
                        .param("filename", "myfile"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(fileDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(fileDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size").value(fileDto.getSize()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mimetype").value(fileDto.getMimetype()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastedited").exists());

        Mockito.verify(fileService, Mockito.times(1)).uploadFile(anyString(), any(MultipartFile.class));
    }

    @Test
    void getFile() throws Exception {

        File file = new File();
        file.setId(1L);
        file.setMimetype(MediaType.TEXT_PLAIN_VALUE);
        file.setFile("Hello".getBytes());

        Mockito.when(fileService.getFile(anyString())).thenReturn(file);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/file").param("filename", "filetodownload")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.MULTIPART_FORM_DATA))
                .andExpect(content().string(containsString("Hello")))
                .andExpect(status().isOk());

        Mockito.verify(fileService, Mockito.times(1)).getFile(anyString());
    }

    @Test
    void editFilename() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/file")
                        .param("filename", "old")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"filename\": \"new\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        Mockito.verify(fileService, Mockito.times(1)).editFilename(anyString(), any(EditNameRequest.class));
    }

    @Test
    void deleteFile() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/file")
                        .param("filename", "filetodelete"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        Mockito.verify(fileService, Mockito.times(1)).deleteFile(anyString());
    }

    @Test
    void listFiles() throws Exception {

        // TODO: можно как-то через стримы-лямбды нагенерить больше дто
        // TODO: контроллер готов, дальше к юнит тесту сервиса надо приступать

        FileDto fileDto = new FileDto(1L, "file", 10L, MediaType.TEXT_PLAIN_VALUE, LocalDateTime.now());

        Page<FileDto> page = new PageImpl<>(Arrays.asList(fileDto));

        Mockito.when(fileService.listFiles(any(), any(), any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/list")).andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)));
    }
}
