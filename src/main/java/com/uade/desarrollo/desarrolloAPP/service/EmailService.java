package com.uade.desarrollo.desarrolloAPP.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendPasswordResetEmail(String to, String token) {
        // Simulación de envío de email
        System.out.println("Simulando envío de email de recuperación de contraseña");
        System.out.println("Para: " + to);
        System.out.println("Token: " + token);
        System.out.println("Link de recuperación: http://localhost:8080/reset-password?token=" + token);
    }
}