package com.example.filesharing.service;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.model.AuthRequest;
import com.example.filesharing.model.AuthResponse;
import com.example.filesharing.repository.UserCredentialsRepository;
import com.example.filesharing.util.JwtUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;


public class UserCredentialsServiceUnitTest {

    @InjectMocks
    UserCredentialsService userCredentialsService;

    @Mock
    UserCredentialsRepository userCredentialsRepository;

    @Mock
    JwtBlackListService jwtBlackListService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void authenticate(){
        AuthResponse authResponseExpected = new AuthResponse("thisisyourjwt");
        when(jwtUtil.generateToken(anyString())).thenReturn("thisisyourjwt");

        AuthResponse authResponse = userCredentialsService.authenticate(new AuthRequest("login", "pass"));

        verify(authenticationManager, times(1)).authenticate(any());
        Assertions.assertEquals(authResponseExpected.getAuthToken(), authResponse.getAuthToken());
    }

    @Test
    void loadUserCredentialsByUsername() {
        UserCredentials userCredentialsExpected = new UserCredentials(1L, "Denis", "pass", true, true, true, true);
        when(userCredentialsRepository.findUserCredentialsWithRoleByUsernameEquals(anyString())).thenReturn(Optional.of(userCredentialsExpected));

        userCredentialsService.loadUserCredentialsByUsername(anyString());

        verify(userCredentialsRepository, times(1)).findUserCredentialsWithRoleByUsernameEquals(anyString());
    }

    @Test
    void loadUserCredentialsByUsername_UsernameNotFoundException() {

        when(userCredentialsRepository.findUserCredentialsWithRoleByUsernameEquals(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> {
                    userCredentialsService.loadUserCredentialsByUsername(anyString());
                }
        );

    }
}