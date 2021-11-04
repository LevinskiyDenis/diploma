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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;

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
        Mockito.when(userCredentialsService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);
        Mockito.when(jwtUtil.generateToken(Mockito.any(UserDetails.class))).thenReturn("thisisyourjwt");

        UserDetails received = userCredentialsService.loadUserByUsername("a");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"user\", \"password\": \"pass\"}")).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.auth-token").doesNotExist());
    }

    @Test
    void login_with_right_credentials() throws Exception {

        Role role = new Role(1L, "USER");
        UserDetails userDetails = new UserCredentials(1L, "denis", "$2a$10$16B7PJ4TXPHpFXl4pOmI2.aL/3RmDdP6WDdO2HHCjI2ui7Xzng3wC", role, true, true, true, true);
        Mockito.when(userCredentialsService.loadUserByUsername(Mockito.anyString())).thenReturn(userDetails);
        Mockito.when(jwtUtil.generateToken(Mockito.any(UserDetails.class))).thenReturn("thisisyourjwt");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"denis\", \"password\": \"12345\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.auth-token").value("thisisyourjwt"));
    }

    @Test
    @WithMockUser
    void logout() throws Exception {

        JwtBlackListEntity jwt = new JwtBlackListEntity(1L, "thisisjwt", System.currentTimeMillis());
        Mockito.when(jwtBlackListService.saveInBlackList(Mockito.anyString())).thenReturn(jwt);

        mockMvc.perform(MockMvcRequestBuilders.post("/logout")
                        .header("auth-token", "Bearer jwt"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

       Mockito.verify(jwtBlackListService, Mockito.times(1)).saveInBlackList(anyString());
    }

}