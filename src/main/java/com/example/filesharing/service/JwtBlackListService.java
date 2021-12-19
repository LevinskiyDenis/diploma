package com.example.filesharing.service;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.repository.JwtBlackListRepository;
import com.example.filesharing.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class JwtBlackListService {

    private final JwtBlackListRepository jwtBlackListRepository;
    private final JwtUtil jwtUtil;

    public JwtBlackListService(JwtBlackListRepository jwtBlackListRepository, JwtUtil jwtUtil) {
        this.jwtBlackListRepository = jwtBlackListRepository;
        this.jwtUtil = jwtUtil;
    }

    public JwtBlackListEntity saveInBlackList(String jwt) {
        Long exp = jwtUtil.extractExpiration(jwt).getTime();

        return jwtBlackListRepository.saveAndFlush(new JwtBlackListEntity(jwt, exp));
    }

    public boolean isBlacklisted(String jwt) {
        return jwtBlackListRepository.findByJwtEquals(jwt).isPresent();
    }
}
