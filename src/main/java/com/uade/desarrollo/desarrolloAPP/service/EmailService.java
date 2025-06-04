package com.uade.desarrollo.desarrolloAPP.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Recuperación de contraseña";
        String text = "Recibiste este email porque solicitaste restablecer tu contraseña.\n\n"
                + "Ingresá al siguiente enlace para continuar:\n"
                + "http://localhost:4002/reset-password?token=" + token + "\n\n"
                + "Este enlace expira en 15 minutos.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
