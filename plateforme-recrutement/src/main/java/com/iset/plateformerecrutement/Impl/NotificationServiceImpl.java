package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.Formation;
import com.iset.plateformerecrutement.model.Notification;
import com.iset.plateformerecrutement.repository.NotificationRepository;
import com.iset.plateformerecrutement.service.EmailService;
import com.iset.plateformerecrutement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification envoyerNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByCandidat(Long candidatId) {
        return notificationRepository.findByCandidatId(candidatId);
    }

    @Override
    public List<Notification> getNotificationsByRecruteur(Long recruteurId) {
        return notificationRepository.findByRecruteurId(recruteurId);
    }

    @Override
    public List<Notification> getNotificationsByFormateur(Long FormateurId) {
        return notificationRepository.findByFormateurId(FormateurId);
    }

    @Override
    public List<Notification> getAllNotification() {
        return notificationRepository.findAll();
    }

    @Override
    public void marquerCommeLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));
        notification.setLu(true);
        notificationRepository.save(notification);
    }
    public List<Notification> getNotificationsForAdmin() {
        return notificationRepository.findAdminNotifications();
    }
}
/*package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.*;
import com.iset.plateformerecrutement.repository.*;
import com.iset.plateformerecrutement.service.EmailService;
import com.iset.plateformerecrutement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final RecruteurRepository recruteurRepository;
    private final FormateurRepository formateurRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   RecruteurRepository recruteurRepository,
                                   FormateurRepository formateurRepository) {
        this.notificationRepository = notificationRepository;
        this.recruteurRepository = recruteurRepository;
        this.formateurRepository = formateurRepository;
    }

    @Override
    public Notification envoyerNotification(Notification notification) {
        if (notification.getDateEnvoi() == null) {
            notification.setDateEnvoi(LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByCandidat(Long candidatId) {
        return notificationRepository.findByCandidatId(candidatId);
    }

    @Override
    public List<Notification> getNotificationsByRecruteur(Long recruteurId) {
        return notificationRepository.findByRecruteurId(recruteurId);
    }

    @Override
    public List<Notification> getNotificationsByFormateur(Long formateurId) {
        return notificationRepository.findByFormateurId(formateurId);
    }

    @Override
    public List<Notification> getAllNotification() {
        return notificationRepository.findAll();
    }

    @Override
    public List<Notification> getNotificationsForAdmin() {
        return notificationRepository.findAdminNotifications();
    }

    // ✅ NOUVEAU : Méthodes pour notifications non lues
    @Override
    public List<Notification> getUnreadNotificationsByCandidat(Long candidatId) {
        return notificationRepository.findUnreadByCandidatId(candidatId);
    }

    @Override
    public List<Notification> getUnreadNotificationsByRecruteur(Long recruteurId) {
        return notificationRepository.findUnreadByRecruteurId(recruteurId);
    }

    @Override
    public List<Notification> getUnreadNotificationsByFormateur(Long formateurId) {
        return notificationRepository.findUnreadByFormateurId(formateurId);
    }

    @Override
    public Long countUnreadByCandidat(Long candidatId) {
        return notificationRepository.countUnreadByCandidatId(candidatId);
    }

    @Override
    public Long countUnreadByRecruteur(Long recruteurId) {
        return notificationRepository.countUnreadByRecruteurId(recruteurId);
    }

    @Override
    public Long countUnreadByFormateur(Long formateurId) {
        return notificationRepository.countUnreadByFormateurId(formateurId);
    }

    @Override
    public void marquerCommeLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification introuvable"));
        notification.setLu(true);
        notificationRepository.save(notification);
    }

    @Override
    public void marquerToutesCommeLues(Long userId, String userType) {
        List<Notification> notifications;

        switch (userType.toUpperCase()) {
            case "CANDIDAT":
                notifications = getUnreadNotificationsByCandidat(userId);
                break;
            case "RECRUTEUR":
                notifications = getUnreadNotificationsByRecruteur(userId);
                break;
            case "FORMATEUR":
                notifications = getUnreadNotificationsByFormateur(userId);
                break;
            default:
                throw new IllegalArgumentException("Type d'utilisateur invalide: " + userType);
        }

        notifications.forEach(n -> n.setLu(true));
        notificationRepository.saveAll(notifications);
    }

    // ✅ NOUVEAU : Méthodes utilitaires
    @Override
    public void creerNotificationCandidature(Long recruteurId, String offreTitre, String candidatNom) {
        try {
            Recruteur recruteur = recruteurRepository.findById(recruteurId)
                    .orElseThrow(() -> new RuntimeException("Recruteur introuvable"));

            Notification notification = new Notification();
            notification.setRecruteur(recruteur);
            notification.setType("NOUVELLE_CANDIDATURE");
            notification.setMessage("Nouvelle candidature reçue de " + candidatNom + " pour l'offre : " + offreTitre);
            notification.setLu(false);
            notification.setDateEnvoi(LocalDateTime.now());

            envoyerNotification(notification);
        } catch (Exception e) {
            System.err.println("Erreur création notification candidature: " + e.getMessage());
        }
    }

    @Override
    public void creerNotificationFormation(Long formateurId, String formationTitre, String candidatNom) {
        try {
            Formateur formateur = formateurRepository.findById(formateurId)
                    .orElseThrow(() -> new RuntimeException("Formateur introuvable"));

            Notification notification = new Notification();
            notification.setFormateur(formateur);
            notification.setType("NOUVELLE_DEMANDE_FORMATION");
            notification.setMessage("Nouvelle demande de formation de " + candidatNom + " pour : " + formationTitre);
            notification.setLu(false);
            notification.setDateEnvoi(LocalDateTime.now());

            envoyerNotification(notification);
        } catch (Exception e) {
            System.err.println("Erreur création notification formation: " + e.getMessage());
        }
    }
}*/
