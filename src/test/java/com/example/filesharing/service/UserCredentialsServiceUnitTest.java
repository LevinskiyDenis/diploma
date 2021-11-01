package com.example.filesharing.service;


import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.repository.UserCredentialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class UserCredentialsServiceUnitTest {

    @InjectMocks
    UserCredentialsService userCredentialsService;

    @Mock
    UserCredentialsRepository userCredentialsRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername() {

        UserDetails userDetailsExpected = new UserCredentials(1L, "Denis", "pass", true, true, true, true);
        when(userCredentialsRepository.findByUsernameEquals(anyString())).thenReturn(java.util.Optional.of(userDetailsExpected));

        UserDetails userDetailsActual = userCredentialsService.loadUserByUsername(anyString());

        Mockito.verify(userCredentialsRepository, Mockito.times(1)).findByUsernameEquals(any());
        assertEquals(userDetailsExpected, userDetailsActual);
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {

        when(userCredentialsRepository.findByUsernameEquals(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,

                () -> {
                    userCredentialsService.loadUserByUsername(anyString());
                }

        );

    }
}