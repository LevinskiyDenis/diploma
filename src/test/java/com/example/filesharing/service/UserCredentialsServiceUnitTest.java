package com.example.filesharing.service;

import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.repository.UserCredentialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;


public class UserCredentialsServiceUnitTest {

    @InjectMocks
    UserCredentialsService userCredentialsService;

    @Mock
    UserCredentialsRepository userCredentialsRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void loadUserByUsername() {
        UserDetails userDetailsExpected = new UserCredentials(1L, "Denis", "pass", true, true, true, true);
        when(userCredentialsRepository.findUserDetailsByUsernameEquals(anyString())).thenReturn(java.util.Optional.of(userDetailsExpected));

        userCredentialsService.loadUserByUsername(anyString());

        verify(userCredentialsRepository, times(1)).findUserDetailsByUsernameEquals(any());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {

        when(userCredentialsRepository.findUserDetailsByUsernameEquals(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,

                () -> {
                    userCredentialsService.loadUserByUsername(anyString());
                }

        );

    }

    @Test
    void loadUserCredentialsByUsername() {
        UserCredentials userCredentialsExpected = new UserCredentials(1L, "Denis", "pass", true, true, true, true);
        when(userCredentialsRepository.findUserCredentialsByUsernameEquals(anyString())).thenReturn(Optional.of(userCredentialsExpected));

        userCredentialsService.loadUserCredentialsByUsername(anyString());

        verify(userCredentialsRepository, times(1)).findUserCredentialsByUsernameEquals(anyString());
    }

    @Test
    void loadUserCredentialsByUsername_UsernameNotFoundException() {

        when(userCredentialsRepository.findUserCredentialsByUsernameEquals(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> {
                    userCredentialsService.loadUserCredentialsByUsername(anyString());
                }
        );

    }
}