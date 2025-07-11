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
        String text = "Recibiste este mail porque solicitaste restablecer tu contraseña.\n\n"
                + "Aqui tienes la clave para el restablecimiento de la contraseña:\n\n"
                +   token + "\n\n"
                + "Esta clave expira en 15 minutos.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
