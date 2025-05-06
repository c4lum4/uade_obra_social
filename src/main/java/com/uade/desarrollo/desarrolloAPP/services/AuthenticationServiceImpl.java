package com.uade.desarrollo.desarrolloAPP.services;

import com.uade.desarrollo.desarrolloAPP.entity.dto.AuthenticationRequest;
import com.uade.desarrollo.desarrolloAPP.entity.dto.AuthenticationResponse;
import com.uade.desarrollo.desarrolloAPP.entity.dto.UserRequest;
import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.repository.UserRepository;
import com.uade.desarrollo.desarrolloAPP.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        // ... otros campos
        userRepository.save(user);

        String jwtToken = jwtUtil.generateToken(user.getUsername());
        return AuthenticationResponse.builder().accessToken(jwtToken).build();

    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtUtil.generateToken(user.getUsername()); // ✅ pasás el username
        return AuthenticationResponse.builder().accessToken(jwtToken).build();

    }
}
