package com.example.security.filters;

import com.example.security.entities.User;
import com.example.security.exceptions.ResourceNotFoundException;
import com.example.security.services.JWTService;
import com.example.security.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import org.springframework.beans.factory.ObjectProvider;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserService userService;
    private final ObjectProvider<HandlerExceptionResolver> resolverProvider;

    public JwtAuthFilter(JWTService jwtService, UserService userService, ObjectProvider<HandlerExceptionResolver> resolverProvider) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.resolverProvider = resolverProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = requestTokenHeader.split("Bearer ")[1];
            Long userId = jwtService.getUserIdFromToken(token);
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {
                    User user = userService.getUserById(userId);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (ResourceNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            resolverProvider.getObject().resolveException(request, response, null, e);
        }
    }
}

