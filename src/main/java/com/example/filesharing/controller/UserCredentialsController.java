package com.example.filesharing.controller;

import com.example.filesharing.entity.JwtBlackListEntity;
import com.example.filesharing.model.AuthRequest;
import com.example.filesharing.model.AuthResponse;
import com.example.filesharing.service.JwtBlackListService;
import com.example.filesharing.service.UserCredentialsService;
import com.example.filesharing.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
@RequestMapping

public class UserCredentialsController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserCredentialsService userCredentialsService;
    private final JwtBlackListService jwtBlackListService;

    @Autowired
    public UserCredentialsController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserCredentialsService userCredentialsService, JwtBlackListService jwtBlackListService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userCredentialsService = userCredentialsService;
        this.jwtBlackListService = jwtBlackListService;
    }

    // не работает метод пост, пишут, что нужно в вебсекьюрит конфиг отключить csrf, но непонятно, почему мы отключаем,
    // если это охраняет нас от атаки
    //https://blog.jdriven.com/2014/10/stateless-spring-security-part-1-stateless-csrf-protection/

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequest authRequest) {
        System.out.println(authRequest);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        final String jwt = jwtUtil.generateToken(userCredentialsService.loadUserByUsername(authRequest.getLogin())); // нужны юзердетейлс, из которых метод дальше сам экстрактнет имя
        return ResponseEntity.ok(new AuthResponse(jwt));
    }


    @PostMapping(value = "/logout")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response, @RequestHeader("auth-token") String authTokenHeader) {

        String jwt = null;

        if (authTokenHeader != null && authTokenHeader.startsWith("Bearer ")) {
            jwt = authTokenHeader.split(" ")[1];
        }

        JwtBlackListEntity jwtBlackListEntity = jwtBlackListService.saveInBlackList(jwt); // сохранили в базе логаученный jwt

        if (jwtBlackListEntity == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


}
