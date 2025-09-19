package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.candidat.id = :candidatId")
    List<Notification> findByCandidatId(Long candidatId);

    @Query("SELECT n FROM Notification n WHERE n.recruteur.id = :recruteurId")
    List<Notification> findByRecruteurId(Long recruteurId);

    @Query("SELECT n FROM Notification n WHERE n.formateur.id = :formateurId")
    List<Notification> findByFormateurId(Long formateurId);

    @Query("SELECT n FROM Notification n WHERE n.isForAdmin = true ORDER BY n.dateEnvoi DESC")
    List<Notification> findAdminNotifications();

}
/*package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ✅ Corrections avec gestion des valeurs nulles
    @Query("SELECT n FROM Notification n WHERE n.candidat.id = :candidatId ORDER BY n.dateEnvoi DESC")
    List<Notification> findByCandidatId(@Param("candidatId") Long candidatId);

    @Query("SELECT n FROM Notification n WHERE n.recruteur.id = :recruteurId ORDER BY n.dateEnvoi DESC")
    List<Notification> findByRecruteurId(@Param("recruteurId") Long recruteurId);

    @Query("SELECT n FROM Notification n WHERE n.formateur.id = :formateurId ORDER BY n.dateEnvoi DESC")
    List<Notification> findByFormateurId(@Param("formateurId") Long formateurId);

    @Query("SELECT n FROM Notification n WHERE n.isForAdmin = true ORDER BY n.dateEnvoi DESC")
    List<Notification> findAdminNotifications();

    // ✅ NOUVEAU : Récupérer les notifications non lues
    @Query("SELECT n FROM Notification n WHERE n.candidat.id = :candidatId AND n.lu = false ORDER BY n.dateEnvoi DESC")
    List<Notification> findUnreadByCandidatId(@Param("candidatId") Long candidatId);

    @Query("SELECT n FROM Notification n WHERE n.recruteur.id = :recruteurId AND n.lu = false ORDER BY n.dateEnvoi DESC")
    List<Notification> findUnreadByRecruteurId(@Param("recruteurId") Long recruteurId);

    @Query("SELECT n FROM Notification n WHERE n.formateur.id = :formateurId AND n.lu = false ORDER BY n.dateEnvoi DESC")
    List<Notification> findUnreadByFormateurId(@Param("formateurId") Long formateurId);

    // ✅ NOUVEAU : Compter les notifications non lues
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.candidat.id = :candidatId AND n.lu = false")
    Long countUnreadByCandidatId(@Param("candidatId") Long candidatId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recruteur.id = :recruteurId AND n.lu = false")
    Long countUnreadByRecruteurId(@Param("recruteurId") Long recruteurId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.formateur.id = :formateurId AND n.lu = false")
    Long countUnreadByFormateurId(@Param("formateurId") Long formateurId);
}
*/