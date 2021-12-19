package com.example.filesharing.controller;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.entity.Role;
import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.model.AuthRequest;
import com.example.filesharing.model.AuthResponse;
import com.example.filesharing.model.UserDetailsImpl;
import com.example.filesharing.service.JwtBlackListService;
import com.example.filesharing.service.UserCredentialsService;
import com.example.filesharing.service.UserDetailsServiceImpl;
import com.example.filesharing.util.JwtUtil;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserCredentialsController.class)
public class UserCredentialsControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    UserCredentialsService userCredentialsService;

    @MockBean
    UserDetailsServiceImpl userDetailsServiceImpl;

    @MockBean
    JwtBlackListService jwtBlackListService;

    @Test
    void login_with_wrong_credentials() throws Exception {

        when(userCredentialsService.authenticate(any(AuthRequest.class))).thenThrow(BadCredentialsException.class);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"user\", \"password\": \"pass\"}")).andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.auth-token").doesNotExist());
    }

    @Test
    void login_with_right_credentials() throws Exception {

        when(userCredentialsService.authenticate(any(AuthRequest.class))).thenReturn(new AuthResponse("thisisyourjwt"));

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

        when(userCredentialsService.logout(any(HttpServletRequest.class), any(HttpServletResponse.class), anyString())).thenReturn(true);

        mockMvc.perform(post("/logout")
                        .header("auth-token", "Bearer jwt"))
                .andDo(print())
                .andExpect(status().isOk());

    }

}