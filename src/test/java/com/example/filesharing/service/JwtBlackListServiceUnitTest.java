package com.example.filesharing.service;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.repository.JwtBlackListRepository;
import com.example.filesharing.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

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
        MockitoAnnotations.openMocks(this);
        jwtBlackListEntity = new JwtBlackListEntity("a", 1L);
    }

    @Test
    void saveInBlackList() {

        when(jwtUtil.extractExpiration(anyString())).thenReturn(new Date());
        when(jwtBlackListRepository.saveInBlackList(any(JwtBlackListEntity.class))).thenReturn(jwtBlackListEntity);

        JwtBlackListEntity jwtBlackListEntityActual = jwtBlackListService.saveInBlackList(anyString());

        assertEquals(jwtBlackListEntity, jwtBlackListEntityActual);
        Mockito.verify(jwtBlackListRepository, Mockito.times(1)).saveInBlackList(any(JwtBlackListEntity.class));

    }


    @Test
    void isBlacklisted() {

        when(jwtBlackListRepository.findByJwtEquals(anyString())).thenReturn(Optional.of(jwtBlackListEntity));

        jwtBlackListService.isBlacklisted(anyString());

        Mockito.verify(jwtBlackListRepository, Mockito.times(1)).findByJwtEquals(anyString());

    }
}