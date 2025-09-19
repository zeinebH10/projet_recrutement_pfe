package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void envoyerEmail(String to, String subject, String body) {
        try {
            logger.info("Tentative d'envoi d'email vers: {}", to);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("zeinebhamila612@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            logger.info("Email envoyé avec succès vers: {}", to);
        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email vers {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Échec de l'envoi de l'email: " + e.getMessage(), e);
        }
    }
}