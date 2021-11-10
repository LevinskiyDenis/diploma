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

    UserCredentialsService userCredentialsService;
    JwtBlackListService jwtBlackListService;
    JwtUtil jwtUtil;

    @Autowired
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

        // Логика ошибок
        // UnsupportedJwtException – if the claimsJws argument does not represent an Claims JWS
        // MalformedJwtException – if the claimsJws string is not a valid JWS
        // SignatureException – if the claimsJws JWS signature validation fails
        // ExpiredJwtException – if the specified JWT is a Claims JWT and the Claims has an expiration time before the time this method is invoked.
        // IllegalArgumentException – if the claimsJws string is null or empty or only whitespace

        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userCredentialsService.loadUserByUsername(login);

            // ему не нужно знать пароль. Он получает данные из хедера и пейлоад, при экстракции генерирует при
            // помощи секрета сигначер. И смотрит, совпадает ли сигначер из хедера со сгенерированной сигначер
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // в аутентикейшн токен ставим дополнительную инфо из сервлетзапроса (айпи и прочие нюансы)
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            // говорит, что это пользователя нужно аутентифицировать
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
