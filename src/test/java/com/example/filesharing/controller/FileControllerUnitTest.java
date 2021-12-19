package com.example.filesharing.controller;

import com.example.filesharing.dto.FileDto;
import com.example.filesharing.entity.File;
import com.example.filesharing.filter.JwtRequestFilter;
import com.example.filesharing.model.EditNameRequest;
import com.example.filesharing.service.FileService;
import com.example.filesharing.service.UserCredentialsService;
import com.example.filesharing.service.UserDetailsServiceImpl;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = FileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FileControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FileService fileService;

    @MockBean
    UserDetailsServiceImpl userDetailsServiceimpl;

    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @Test
    void uploadFile() throws Exception {

        FileDto fileDto = new FileDto(1L, "file", 10L, MediaType.TEXT_PLAIN_VALUE, LocalDateTime.now());
        when(fileService.uploadFile(anyString(), Mockito.any(MultipartFile.class))).thenReturn(fileDto);

        MockMultipartFile mockMultipartFile
                = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/file").file(mockMultipartFile)
                        .param("filename", "myfile"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(fileDto.getId()))
                .andExpect(jsonPath("$.name").value(fileDto.getName()))
                .andExpect(jsonPath("$.size").value(fileDto.getSize()))
                .andExpect(jsonPath("$.mimetype").value(fileDto.getMimetype()))
                .andExpect(jsonPath("$.lastedited").exists());

        verify(fileService, times(1)).uploadFile(anyString(), Mockito.any(MultipartFile.class));
    }

    @Test
    void getFile() throws Exception {

        File file = new File();
        file.setId(1L);
        file.setMimetype(MediaType.TEXT_PLAIN_VALUE);
        file.setFile("Hello".getBytes());

        when(fileService.getFile(anyString())).thenReturn(file);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/file").param("filename", "filetodownload")
                )
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.MULTIPART_FORM_DATA))
                .andExpect(content().string(containsString("Hello")))
                .andExpect(status().isOk());

        verify(fileService, times(1)).getFile(anyString());
    }

    @Test
    void editFilename() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.put("/file")
                        .param("filename", "old")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"filename\": \"new\"}"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(fileService, times(1)).editFilename(anyString(), Mockito.any(EditNameRequest.class));
    }

    @Test
    void deleteFile() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/file")
                        .param("filename", "filetodelete"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(fileService, times(1)).deleteFile(anyString());
    }

    @Test
    void listFiles() throws Exception {
        FileDto fileDto = new FileDto(1L, "file", 10L, MediaType.TEXT_PLAIN_VALUE, LocalDateTime.now());

        Page<FileDto> page = new PageImpl<>(Arrays.asList(fileDto));

        when(fileService.listFiles(any(), any(), any())).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/list")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }
}
