package com.defence.studentapp.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        /*
         * Look for the JWT inside the "token" cookie.
         */
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {

                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        /*
         * No token cookie found.
         * Continue the request without authentication.
         */
        if (token == null || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            /*
             * Get username and role from the JWT.
             */
            String username = jwtService.extractUsername(token);
            String role = jwtService.extractRole(token);

            /*
             * Create authentication information.
             */
            if (username != null &&
                    SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority("ROLE_" + role);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username,
                                null,
                                Collections.singletonList(authority)
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                /*
                 * Tell Spring Security that the user is authenticated.
                 */
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception e) {

            /*
             * Invalid or expired JWT.
             */
            SecurityContextHolder.clearContext();
        }

        /*
         * Continue processing the request.
         */
        filterChain.doFilter(request, response);
    }
}