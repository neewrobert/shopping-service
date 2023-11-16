package com.neewrobert.shopping.infrastructure.web.filter;

import com.neewrobert.shopping.domain.model.User;
import com.neewrobert.shopping.domain.service.security.JwtService;
import com.neewrobert.shopping.infrastructure.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthenticationFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            processJwtAuthentication(authorization.substring(7), request, response);
        }

        filterChain.doFilter(request, response);

    }

    private void processJwtAuthentication(String jwt, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        final String username = jwtService.extractUsername(jwt);

        if (username.isBlank() || SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        UserDetails user;
        try {
            user = customUserDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid authentication");
            return;
        }

        if (jwtService.isTokenValid(jwt, User.build(user.getUsername(), user.getUsername(), user.getPassword()))) {
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
