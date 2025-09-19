package com.iset.plateformerecrutement.service;

public interface EmailService {
    void envoyerEmail(String to, String subject, String body);
}