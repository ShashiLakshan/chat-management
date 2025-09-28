package com.mychat.mychat.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;


@Component
@Order(1)
public class ApiKeyAuthFilter extends OncePerRequestFilter {
    @Value("${security.api-key}")
    private String expectedKey;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String key = req.getHeader("X-API-Key");
        if (this.expectedKey == null || this.expectedKey.isBlank() || !Objects.equals(this.expectedKey, key)) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType("application/json");
            res.getWriter().write("{\"message\":\"Unauthorized\"}");
            return;
        }

        if (req.getHeader("X-User-Id") == null) {
            res.setStatus(HttpStatus.BAD_REQUEST.value());
            res.getWriter().write("{\"message\":\"X-User-Id header required\"}");
            return;
        }
        chain.doFilter(req, res);
    }
}