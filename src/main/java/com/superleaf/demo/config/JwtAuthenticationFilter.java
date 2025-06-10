package com.superleaf.demo.config;

import java.io.IOException;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.superleaf.demo.security.JwtUtil;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest req, 
        @NonNull HttpServletResponse res,
        @NonNull FilterChain filterChain
    )throws ServletException,IOException
    {
        final String authHeader=req.getHeader(  "Authorization");
        final String jwt;
        final String username;
        if (authHeader==null||authHeader.startsWith("Bearer ")){
            filterChain.doFilter(req, res);
            return;
        }
        jwt=authHeader.substring(7);
        username= jwtUtil.extractUsername(jwt);
        
    }
}
