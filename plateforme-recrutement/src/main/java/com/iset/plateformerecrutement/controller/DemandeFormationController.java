package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.model.DemandeFormation;
import com.iset.plateformerecrutement.model.DemandeStage;
import com.iset.plateformerecrutement.model.Notification;
import com.iset.plateformerecrutement.requests.DemandeFormationRequest;
import com.iset.plateformerecrutement.service.DemandeFormationService;
import com.iset.plateformerecrutement.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/demande-formation")
public class DemandeFormationController {

    private final DemandeFormationService demandeFormationService;
    private final NotificationService notificationService;

    // Injection du service
    public DemandeFormationController(DemandeFormationService demandeFormationService, NotificationService notificationService) {
        this.demandeFormationService = demandeFormationService;
        this.notificationService = notificationService;
    }
    @GetMapping("/formateur/{formateurId}")
    public List<DemandeFormation> getDemandesByFormateur(@PathVariable Long formateurId){
        return demandeFormationService.getDemandesByFormateurId(formateurId);
    }
    // Créer une demande de formation
    @PostMapping("/{candidatId}/{formationId}")
    public ResponseEntity<?> createDemandeFormation(
            @PathVariable Long candidatId,
            @PathVariable Long formationId,
            @RequestBody DemandeFormationRequest request) {

        try {
            // 1. Enregistrer la demande de formation
            DemandeFormation savedDemandeformation = demandeFormationService.saveDemandeFormation(candidatId, formationId, request);

            // 2. Créer une notification pour le formateur
            if (savedDemandeformation != null && savedDemandeformation.getFormation() != null) {
                Notification notification = new Notification();
                notification.setMessage("Un candidat a postulé à votre formation.");
                notification.setFormateur(savedDemandeformation.getFormation().getFormateur());
                notification.setType("POSTULATION_FORMATION");
                notification.setLu(false);
                notificationService.envoyerNotification(notification);
            }

            return ResponseEntity.ok(savedDemandeformation);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Le candidat a déjà postulé à cette formation.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    // Mettre à jour une demande de formation
    @PutMapping("/update/{id}")
    public ResponseEntity<DemandeFormation> updateDemandeFormation(
            @PathVariable Long id,
            @RequestBody DemandeFormationRequest request) {

        DemandeFormation updatedDemandeFormation = demandeFormationService.updateDemandeFormation(id, request);
        return ResponseEntity.ok(updatedDemandeFormation);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DemandeFormation>> getAllDemandeFormations() {
        List<DemandeFormation> demandes = demandeFormationService.getAllDemandeFormation();
        return ResponseEntity.ok(demandes);
    }

    // Récupérer les demandes par ID de candidat
    @GetMapping("/candidat/{candidatId}")
    public ResponseEntity<List<DemandeFormation>> getDemandeByCandidatId(@PathVariable Long candidatId) {
        List<DemandeFormation> demandes = demandeFormationService.getDemandeFormationByCandidatId(candidatId);
        return ResponseEntity.ok(demandes);
    }

    // Récupérer les demandes par ID de formation
    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<DemandeFormation>> getDemandeByFormationId(@PathVariable Long formationId) {
        List<DemandeFormation> demandes = demandeFormationService.getDemandeFormationByFormationId(formationId);
        return ResponseEntity.ok(demandes);
    }

    // Supprimer une demande de formation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemandeFormation(@PathVariable Long id) {
        demandeFormationService.deleteDemandeFormation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/traiter/{id}")
    public ResponseEntity<Void> traiterDemande(@PathVariable Long id, @RequestParam boolean accepte) {
        demandeFormationService.traiterDemande(id, accepte);
        return ResponseEntity.ok().build();
    }
}
