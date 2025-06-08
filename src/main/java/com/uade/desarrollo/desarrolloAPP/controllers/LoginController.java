package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.security.JwtUtil;
import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<Object> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User existingUser = userRepository.findByUsername(username).orElse(null);
        if (existingUser != null && existingUser.getPassword().equals(password)) {
            String token = jwtUtil.generateToken(username);

            // Excluir la contraseña del usuario en la respuesta
            existingUser.setPassword(null);

            // Crear una respuesta con un mensaje y los datos del usuario
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inicio de sesión exitoso");
            response.put("user", existingUser);
            response.put("token", token);

            return ResponseEntity.ok(response);
        } else {
            throw new com.uade.desarrollo.desarrolloAPP.exceptions.BusinessException("Credenciales incorrectas");
        }
    }
}
