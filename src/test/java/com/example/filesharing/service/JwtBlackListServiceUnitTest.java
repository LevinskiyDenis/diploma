package com.example.filesharing.service;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.repository.JwtBlackListRepository;
import com.example.filesharing.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

public class JwtBlackListServiceUnitTest {

    @InjectMocks
    JwtBlackListService jwtBlackListService;

    @Mock
    JwtBlackListRepository jwtBlackListRepository;

    @Mock
    JwtUtil jwtUtil;

    JwtBlackListEntity jwtBlackListEntity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        jwtBlackListEntity = new JwtBlackListEntity("a", 1L);
    }

    @Test
    void saveInBlackList() {
        when(jwtUtil.extractExpiration(anyString())).thenReturn(new Date());
        when(jwtBlackListRepository.saveAndFlush(any(JwtBlackListEntity.class))).thenReturn(jwtBlackListEntity);

        jwtBlackListService.saveInBlackList(anyString());

        verify(jwtUtil, times(1)).extractExpiration(anyString());
        verify(jwtBlackListRepository, times(1)).saveAndFlush(any(JwtBlackListEntity.class));
    }


    @Test
    void isBlacklisted() {
        when(jwtBlackListRepository.findByJwtEquals(anyString())).thenReturn(Optional.of(jwtBlackListEntity));

        jwtBlackListService.isBlacklisted(anyString());

        verify(jwtBlackListRepository, times(1)).findByJwtEquals(anyString());
    }
}