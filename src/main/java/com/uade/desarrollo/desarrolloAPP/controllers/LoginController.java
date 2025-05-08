package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.security.JwtUtil;
import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public String login(@RequestBody User user) {
        // Verificar si el usuario existe y la contraseña es correcta
        User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existingUser != null && existingUser.getPassword().equals(user.getPassword())) {
            // Generar el token JWT
            String token = jwtUtil.generateToken(user.getUsername());
            return token;
        } else {
            throw new RuntimeException("Credenciales incorrectas");
        }
    }
}
