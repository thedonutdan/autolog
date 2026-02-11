package org.thedonutdan.autolog.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;


import org.thedonutdan.autolog.DAO.UserDAO;
import org.thedonutdan.autolog.security.JwtAuthFilter;
import org.thedonutdan.autolog.security.JwtUtil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * Spring boot security configuration
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())                           // use your CorsConfig
            .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()// preflight
                .requestMatchers("/api/public/**", "/auth/**").permitAll()
                .anyRequest().authenticated()                          // protect the rest
            )
            .exceptionHandling(e -> e
                .authenticationEntryPoint((req,res,ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler((req,res,ex) -> res.sendError(HttpServletResponse.SC_FORBIDDEN))
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter(JwtUtil jwtUtil, UserDAO userDAO) {
        return new JwtAuthFilter(jwtUtil, userDAO);
    }
}
