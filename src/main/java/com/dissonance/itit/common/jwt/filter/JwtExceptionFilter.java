package com.dissonance.itit.common.jwt.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            response.setContentType("application/json;charset=UTF-8");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            response.setCharacterEncoding("utf-8");

            ObjectMapper mapper = new ObjectMapper();

            ObjectNode errorJson = mapper.createObjectNode();

            errorJson.put("status", HttpStatus.UNAUTHORIZED.value());
            errorJson.put("message", e.getMessage());
            errorJson.put("timestamp", LocalDateTime.now().toString());

            response.getWriter().write(mapper.writeValueAsString(errorJson));
        }
    }
}