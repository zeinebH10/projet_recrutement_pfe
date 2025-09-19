package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.model.Formation;
import com.iset.plateformerecrutement.model.Notification;
import com.iset.plateformerecrutement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public Notification envoyerNotification(@RequestBody Notification notification) {
        return notificationService.envoyerNotification(notification);
    }

    @GetMapping("/candidat/{candidatId}")
    public List<Notification> getNotificationsByCandidat(@PathVariable Long candidatId) {
        return notificationService.getNotificationsByCandidat(candidatId);
    }
    @GetMapping("/formateur/{formateurId}")
    public List<Notification> getNotificationsByformateur(@PathVariable Long formateurId) {
        return notificationService.getNotificationsByFormateur(formateurId);
    }
    @GetMapping("/recruteur/{recruteurId}")
    public List<Notification> getNotificationsByRecruteur(@PathVariable Long recruteurId) {
        return notificationService.getNotificationsByRecruteur(recruteurId);
    }

    @PutMapping("/marquer-lue/{notificationId}")
    public ResponseEntity<Void> marquerCommeLue(@PathVariable Long notificationId) {
        notificationService.marquerCommeLue(notificationId);
        return ResponseEntity.ok().build();
    }
    @GetMapping( "/all")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotification();
    }

    @GetMapping("/admin")
    public List<Notification> getNotificationsForAdmin() {
    return notificationService.getNotificationsForAdmin();
      }
}
/*package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.model.Notification;
import com.iset.plateformerecrutement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public Notification envoyerNotification(@RequestBody Notification notification) {
        return notificationService.envoyerNotification(notification);
    }

    // ✅ Notifications par type d'utilisateur
    @GetMapping("/candidat/{candidatId}")
    public List<Notification> getNotificationsByCandidat(@PathVariable Long candidatId) {
        return notificationService.getNotificationsByCandidat(candidatId);
    }

    @GetMapping("/recruteur/{recruteurId}")
    public List<Notification> getNotificationsByRecruteur(@PathVariable Long recruteurId) {
        return notificationService.getNotificationsByRecruteur(recruteurId);
    }

    @GetMapping("/formateur/{formateurId}")
    public List<Notification> getNotificationsByFormateur(@PathVariable Long formateurId) {
        return notificationService.getNotificationsByFormateur(formateurId);
    }

    // ✅ NOUVEAU : Notifications non lues
    @GetMapping("/candidat/{candidatId}/unread")
    public List<Notification> getUnreadNotificationsByCandidat(@PathVariable Long candidatId) {
        return notificationService.getUnreadNotificationsByCandidat(candidatId);
    }

    @GetMapping("/recruteur/{recruteurId}/unread")
    public List<Notification> getUnreadNotificationsByRecruteur(@PathVariable Long recruteurId) {
        return notificationService.getUnreadNotificationsByRecruteur(recruteurId);
    }

    @GetMapping("/formateur/{formateurId}/unread")
    public List<Notification> getUnreadNotificationsByFormateur(@PathVariable Long formateurId) {
        return notificationService.getUnreadNotificationsByFormateur(formateurId);
    }

    // ✅ NOUVEAU : Compter les non lues
    @GetMapping("/candidat/{candidatId}/count")
    public ResponseEntity<Map<String, Long>> getUnreadCountByCandidat(@PathVariable Long candidatId) {
        Long count = notificationService.countUnreadByCandidat(candidatId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/recruteur/{recruteurId}/count")
    public ResponseEntity<Map<String, Long>> getUnreadCountByRecruteur(@PathVariable Long recruteurId) {
        Long count = notificationService.countUnreadByRecruteur(recruteurId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/formateur/{formateurId}/count")
    public ResponseEntity<Map<String, Long>> getUnreadCountByFormateur(@PathVariable Long formateurId) {
        Long count = notificationService.countUnreadByFormateur(formateurId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    // ✅ Marquer comme lue
    @PutMapping("/marquer-lue/{notificationId}")
    public ResponseEntity<Void> marquerCommeLue(@PathVariable Long notificationId) {
        notificationService.marquerCommeLue(notificationId);
        return ResponseEntity.ok().build();
    }

    // ✅ NOUVEAU : Marquer toutes comme lues
    @PutMapping("/marquer-toutes-lues/{userId}/{userType}")
    public ResponseEntity<Void> marquerToutesCommeLues(
            @PathVariable Long userId,
            @PathVariable String userType) {
        notificationService.marquerToutesCommeLues(userId, userType);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotification();
    }

    @GetMapping("/admin")
    public List<Notification> getNotificationsForAdmin() {
        return notificationService.getNotificationsForAdmin();
    }
}*/