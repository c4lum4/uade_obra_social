package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.dto.AuthenticationRequest;
import com.uade.desarrollo.desarrolloAPP.entity.dto.AuthenticationResponse;
import com.uade.desarrollo.desarrolloAPP.entity.dto.UserRequest;
import com.uade.desarrollo.desarrolloAPP.security.JwtUtil;
import com.uade.desarrollo.desarrolloAPP.services.AuthenticationService;
import com.uade.desarrollo.desarrolloAPP.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        String token = extractToken(authHeader);
        if (token != null && jwtUtil.isTokenValid(token, jwtUtil.extractUsername(token))) {
            var user = userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
