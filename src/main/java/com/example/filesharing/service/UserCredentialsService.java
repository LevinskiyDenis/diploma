package com.example.filesharing.service;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.entity.UserCredentials;
import com.example.filesharing.model.AuthRequest;
import com.example.filesharing.model.AuthResponse;
import com.example.filesharing.repository.UserCredentialsRepository;
import com.example.filesharing.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service

public class UserCredentialsService {

    private final AuthenticationManager authenticationManager;
    private final UserCredentialsRepository userCredentialsRepository;
    private final JwtUtil jwtUtil;
    private final JwtBlackListService jwtBlackListService;

    public UserCredentialsService(AuthenticationManager authenticationManager, UserCredentialsRepository userCredentialsRepository, JwtUtil jwtUtil, JwtBlackListService jwtBlackListService) {
        this.authenticationManager = authenticationManager;
        this.userCredentialsRepository = userCredentialsRepository;
        this.jwtUtil = jwtUtil;
        this.jwtBlackListService = jwtBlackListService;
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        final String jwt = jwtUtil.generateToken(authRequest.getLogin());
        return new AuthResponse(jwt);
    }

    public boolean logout(HttpServletRequest request, HttpServletResponse response, String authTokenHeader) {

        String jwt = authTokenHeader.split(" ")[1];
        JwtBlackListEntity jwtBlackListEntity = jwtBlackListService.saveInBlackList(jwt);
        if (jwtBlackListEntity == null) return false;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return true;
    }

    public UserCredentials loadUserCredentialsByUsername(String username) throws UsernameNotFoundException {
        return userCredentialsRepository.findUserCredentialsWithRoleByUsernameEquals(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
