package com.savpoint.savpoint.configurations;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.savpoint.savpoint.service.configurations.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    public SecurityFilter(TokenService tokenService, UserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);

        if (token != null) {
            try {
                String email = tokenService.validateToken(token);

                if (email != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    if (userDetails != null) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (JWTVerificationException exception) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token inválido ou expirado.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken (HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");

        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
