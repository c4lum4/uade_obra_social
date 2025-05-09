package com.uade.desarrollo.desarrolloAPP.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // Constructor para inyectar el JwtAuthFilter
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/api/auth/register", "/api/auth/login", "/login", "/api/profesionales/**", "/api/turnos/**", "/api/obras-sociales/**").permitAll()  // Permitir acceso a obras sociales
                .requestMatchers("/users/**").permitAll()  // Permitir acceso a los endpoints bajo '/users/**'
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Permitir acceso a recursos estáticos
                .anyRequest().authenticated()  // Requiere autenticación para todas las demás solicitudes
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);  // Asegurarse de que el filtro JWT se aplique correctamente

        return http.build();
    }
}
