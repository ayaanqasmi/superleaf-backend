package com.superleaf.demo.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.superleaf.demo.security.JwtUtil;
import com.superleaf.demo.security.token.TokenRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    private static final String[] WHITELIST = {
        "/api/auth/register",
        "/api/auth/authenticate"
    };

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Skip filtering for whitelisted URLs
        String path = request.getServletPath();
        for (String url : WHITELIST) {
            if (path.startsWith(url)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        final String authHeader = request.getHeader("Authorization");
        log.info("Processing request to: {}", path);
        log.info("Authorization header: {}", authHeader);

        final String jwt;
        final String username;
        // Proper null + prefix check
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Invalid or missing Authorization header");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        username = jwtUtil.extractUsername(jwt);
        log.info("Extracted username from token: {}", username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked())
                .orElse(false);
            
            log.info("Token valid in repository: {}", isTokenValid);
            log.info("JWT validation result: {}", jwtUtil.isTokenValid(jwt, userDetails));
            
            if (jwtUtil.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Authentication set for user: {}", username);
            } else {
                log.warn("Token validation failed for user: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}
