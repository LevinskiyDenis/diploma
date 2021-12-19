package com.example.filesharing.controller;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.model.AuthRequest;
import com.example.filesharing.model.AuthResponse;
import com.example.filesharing.service.JwtBlackListService;
import com.example.filesharing.service.UserCredentialsService;
import com.example.filesharing.util.JwtUtil;
import lombok.AllArgsConstructor;
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

    private final UserCredentialsService userCredentialsService;

    public UserCredentialsController(UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(userCredentialsService.authenticate(authRequest));
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response, @RequestHeader("auth-token") String authTokenHeader) {

        if (userCredentialsService.logout(request, response, authTokenHeader)) {
            return ResponseEntity.ok("Successful logout");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


}
