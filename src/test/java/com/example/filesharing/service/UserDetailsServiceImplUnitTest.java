package com.example.filesharing.service;

import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.repository.UserCredentialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserDetailsServiceImplUnitTest {

    @InjectMocks
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    UserCredentialsRepository userCredentialsRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void loadUserByUsername() {
        UserCredentials userCredentials = new UserCredentials(1L, "Denis", "pass", true, true, true, true);
        when(userCredentialsRepository.findUserCredentialsWithRoleByUsernameEquals(anyString())).thenReturn(Optional.of(userCredentials));

        userDetailsServiceImpl.loadUserByUsername(anyString());

        verify(userCredentialsRepository, times(1)).findUserCredentialsWithRoleByUsernameEquals(any());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {

        when(userCredentialsRepository.findUserCredentialsWithRoleByUsernameEquals(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,

                () -> {
                    userDetailsServiceImpl.loadUserByUsername(anyString());
                }

        );

    }
}
