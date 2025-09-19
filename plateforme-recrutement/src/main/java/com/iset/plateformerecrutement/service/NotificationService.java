package com.iset.plateformerecrutement.service;

import com.iset.plateformerecrutement.model.Notification;

import java.util.List;

public interface NotificationService {
    Notification envoyerNotification(Notification notification);
    List<Notification> getNotificationsByCandidat(Long candidatId);
    List<Notification> getNotificationsByRecruteur(Long recruteurId);
    List<Notification> getNotificationsByFormateur(Long FormateurId);
    void marquerCommeLue(Long notificationId);
    List<Notification> getAllNotification();
    List<Notification> getNotificationsForAdmin();
}
