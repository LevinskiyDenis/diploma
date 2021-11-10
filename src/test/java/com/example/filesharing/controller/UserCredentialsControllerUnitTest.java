package com.example.filesharing.controller;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.entity.Role;
import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.service.JwtBlackListService;
import com.example.filesharing.service.UserCredentialsService;
import com.example.filesharing.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserCredentialsController.class)
public class UserCredentialsControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    UserCredentialsService userCredentialsService;

    @MockBean
    JwtBlackListService jwtBlackListService;

    @Test
    void login_with_wrong_credentials() throws Exception {

        Role role = new Role(1L, "USER");
        UserDetails userDetails = new UserCredentials(1L, "denis", "$2a$10$16B7PJ4TXPHpFXl4pOmI2.aL/3RmDdP6WDdO2HHCjI2ui7Xzng3wC", role, true, true, true, true);
        when(userCredentialsService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);
        when(jwtUtil.generateToken(Mockito.any(UserDetails.class))).thenReturn("thisisyourjwt");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"user\", \"password\": \"pass\"}")).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.auth-token").doesNotExist());
    }

    @Test
    void login_with_right_credentials() throws Exception {

        Role role = new Role(1L, "USER");
        UserDetails userDetails = new UserCredentials(1L, "denis", "$2a$10$16B7PJ4TXPHpFXl4pOmI2.aL/3RmDdP6WDdO2HHCjI2ui7Xzng3wC", role, true, true, true, true);
        when(userCredentialsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.generateToken(Mockito.any(UserDetails.class))).thenReturn("thisisyourjwt");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"denis\", \"password\": \"12345\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.auth-token").value("thisisyourjwt"));
    }

    @Test
    @WithMockUser
    void logout() throws Exception {

        JwtBlackListEntity jwt = new JwtBlackListEntity(1L, "thisisjwt", System.currentTimeMillis());
        when(jwtBlackListService.saveInBlackList(anyString())).thenReturn(jwt);

        mockMvc.perform(post("/logout")
                        .header("auth-token", "Bearer jwt"))
                .andDo(print())
                .andExpect(status().isOk());

      verify(jwtBlackListService, times(1)).saveInBlackList(anyString());
    }

}