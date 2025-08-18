package io.thedonutdan.vehiclemaintenance.security;

import java.io.IOError;
import java.io.IOException;

import java.util.Collections;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import io.micrometer.common.lang.NonNull;
import io.thedonutdan.vehiclemaintenance.DAO.UserDAO;
import io.thedonutdan.vehiclemaintenance.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDAO userDAO;

    public JwtAuthFilter(JwtUtil jwtUtil, UserDAO userDAO) {
        this.jwtUtil = jwtUtil;
        this.userDAO = userDAO;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            System.out.println("[JwtFilter] " + request.getMethod() + " " + request.getRequestURI()
                + " cookies=" + (request.getCookies() == null ? 0 : request.getCookies().length));

            if (request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if ("jwt".equals(c.getName())) {
                        String v = c.getValue();
                        int n = (v == null ? 0 : v.length());
                        String snippet = (v == null) ? "null"
                                : (n <= 16 ? v : (v.substring(0, 8) + "..." + v.substring(n - 8)));
                        System.out.println("[JwtFilter] found jwt cookie len=" + n + " token=" + snippet);
                        try {

                            UUID userId = jwtUtil.parseToken(c.getValue());
                            User user = userDAO.findById(userId);
                            if (user != null) {
                                var auth = new UsernamePasswordAuthenticationToken(
                                user.getUserId(),
                                null,
                                Collections.emptyList() // or user roles: List.of(new SimpleGrantedAuthority("ROLE_USER"))
                            );
                            SecurityContextHolder.getContext().setAuthentication(auth);
                            System.out.println("[JwtFilter] authenticated principal=" + user.getUserId());
                            } else {
                                System.out.println("[JwtFilter] user not found for id=" + userId);
                            }
                        } catch (JwtException | IllegalArgumentException e) {
                            System.out.println("Error in token parse:" + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
        System.out.println("[JwtFilter] " + request.getMethod() + " " + request.getRequestURI()
            + " -> status " + response.getStatus());
    }
}
