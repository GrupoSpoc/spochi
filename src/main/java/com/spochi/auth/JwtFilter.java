package com.spochi.auth;

import com.spochi.auth.firebase.AuthorizationException;
import com.spochi.service.authenticate.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    public static final String INVALID_TOKEN_MESSAGE = "Invalid or expired token";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_SUFFIX = "Bearer ";

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    private static final List<String> skippedEndpoints;

    static {
        skippedEndpoints = new ArrayList<>();

        skippedEndpoints.add("/authenticate");
        skippedEndpoints.add("/ping");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {

            if (!skippedEndpoints.contains(httpServletRequest.getRequestURI())) {
                String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);

                if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_SUFFIX)) {
                    throw new AuthorizationException();
                }

                String token = authorizationHeader.substring(BEARER_SUFFIX.length());

                if (!jwtUtil.isTokenValid(token)) {
                    throw new AuthorizationException();
                }
            }

            filterChain.doFilter(httpServletRequest, httpServletResponse);

        } catch (AuthorizationException | JwtException e) {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.getWriter().write(INVALID_TOKEN_MESSAGE);
        }
    }
}
