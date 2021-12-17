package com.example.filesharing.filter;

import com.example.filesharing.service.JwtBlackListService;
import com.example.filesharing.service.UserCredentialsService;
import com.example.filesharing.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserCredentialsService userCredentialsService;
    private final JwtBlackListService jwtBlackListService;
    private final JwtUtil jwtUtil;

    public JwtRequestFilter(UserCredentialsService userCredentialsService, JwtBlackListService jwtBlackListService, JwtUtil jwtUtil) {
        this.userCredentialsService = userCredentialsService;
        this.jwtBlackListService = jwtBlackListService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String authTokenHeader = httpServletRequest.getHeader("auth-token");

        String login = null;
        String jwt = null;

        if (authTokenHeader != null && authTokenHeader.startsWith("Bearer ")) {
            jwt = authTokenHeader.split(" ")[1];
            login = jwtUtil.extractUsername(jwt); // по сути здесь валидация всего происходит
        }


        if (jwt != null && jwtBlackListService.isBlacklisted(jwt)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userCredentialsService.loadUserByUsername(login);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
