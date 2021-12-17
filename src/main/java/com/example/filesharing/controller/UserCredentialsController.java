package com.example.filesharing.controller;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.model.AuthRequest;
import com.example.filesharing.model.AuthResponse;
import com.example.filesharing.service.JwtBlackListService;
import com.example.filesharing.service.UserCredentialsService;
import com.example.filesharing.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")

public class UserCredentialsController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserCredentialsService userCredentialsService;
    private final JwtBlackListService jwtBlackListService;

    public UserCredentialsController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserCredentialsService userCredentialsService, JwtBlackListService jwtBlackListService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userCredentialsService = userCredentialsService;
        this.jwtBlackListService = jwtBlackListService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        final String jwt = jwtUtil.generateToken(userCredentialsService.loadUserByUsername(authRequest.getLogin()));
        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, @RequestHeader("auth-token") String authTokenHeader) {

        String jwt = authTokenHeader.split(" ")[1];

        JwtBlackListEntity jwtBlackListEntity = jwtBlackListService.saveInBlackList(jwt); // сохранили в базе логаученный jwt

        if (jwtBlackListEntity == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return ResponseEntity.ok("Successful logout");
    }


}
