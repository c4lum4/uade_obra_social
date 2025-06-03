package com.uade.desarrollo.desarrolloAPP.controllers;

import com.uade.desarrollo.desarrolloAPP.entity.User;
import com.uade.desarrollo.desarrolloAPP.entity.PasswordResetToken;
import com.uade.desarrollo.desarrolloAPP.repository.UserRepository;
import com.uade.desarrollo.desarrolloAPP.repository.PasswordResetTokenRepository;
import com.uade.desarrollo.desarrolloAPP.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordResetTokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;    @PostMapping("/request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                "success", false,
                "message", "No existe usuario con ese email"
            ));
        }

        User user = userOpt.get();
        
        // Invalidar tokens anteriores
        Optional<PasswordResetToken> existingToken = tokenRepository.findByUserEmail(email);
        existingToken.ifPresent(token -> tokenRepository.delete(token));

        // Crear nuevo token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);        // Simular envío de email
        emailService.sendPasswordResetEmail(user.getEmail(), token);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Se ha enviado un email con las instrucciones para restablecer la contraseña",
            "token", token  // Agregamos el token a la respuesta
        ));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired() || tokenOpt.get().isUsed()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Token inválido o expirado"
            ));
        }

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Token válido"
        ));
    }

    @PutMapping("/reset")
    public ResponseEntity<?> resetPassword(
            @RequestParam String token,
            @RequestBody Map<String, String> body) {

        String newPassword = body.get("newPassword");
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);

        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired() || tokenOpt.get().isUsed()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Token inválido o expirado"
            ));
        }

        PasswordResetToken resetToken = tokenOpt.get();
        User user = resetToken.getUser();
        
        user.setPassword(passwordEncoder.encode(newPassword));
        resetToken.setUsed(true);
        
        tokenRepository.save(resetToken);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Contraseña actualizada correctamente"
        ));
    }
}
