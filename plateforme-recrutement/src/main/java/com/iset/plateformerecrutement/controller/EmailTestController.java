package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<?> testEmail(@RequestParam String to) {
        try {
            emailService.envoyerEmail(
                    to,
                    "Test Email - Spring Boot",
                    "Ceci est un email de test depuis votre application Spring Boot."
            );
            return ResponseEntity.ok("Email envoyé avec succès à " + to);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erreur: " + e.getMessage());
        }
    }

    @GetMapping("/test-email")
    public String testEmail() {
        try {
            emailService.envoyerEmail(
                    "zeinebhamila612@gmail.com",
                    "Test",
                    "Email de test"
            );
            return "Email envoyé !";
        } catch (Exception e) {
            return "Erreur: " + e.getMessage();
        }
    }
}
