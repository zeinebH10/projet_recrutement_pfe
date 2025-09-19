package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.DemandeStageServiceImpl;
import com.iset.plateformerecrutement.model.Candidat;
import com.iset.plateformerecrutement.model.DemandeStage;
import com.iset.plateformerecrutement.model.Notification;
import com.iset.plateformerecrutement.requests.CandidatUpdateRequest;
import com.iset.plateformerecrutement.service.DemandeService;
import com.iset.plateformerecrutement.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("/demande-stage")
public class DemandeStageController {

    private final DemandeStageServiceImpl demandeStageService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    public DemandeStageController(DemandeStageServiceImpl demandeStageService) {
        this.demandeStageService = demandeStageService;
    }

    @GetMapping("/recruteur/{id}")
    public List<DemandeStage> getDemandesByRecruteur(@PathVariable("id") Long recruteurId) {
        return demandeStageService.getDemandesByRecruteurId(recruteurId);
    }
    // ✅ Récupérer toutes les demandes de stage
    @GetMapping("/all")
    public List<DemandeStage> getAllDemandeStages() {
        return demandeStageService.getAllDemandeStage();
    }

    // ✅ Récupérer les demandes de stage par ID de candidat
    @GetMapping("/candidat/{candidatId}")
    public List<DemandeStage> getDemandeStageByCandidatId(@PathVariable Long candidatId) {
        return demandeStageService.getDemandeStageByCandidatId(candidatId);
    }

    // ✅ Récupérer une demande de stage par ID
    @GetMapping("/{id}")
    public Optional<DemandeStage> getDemandeStageById(@PathVariable Long id) {
        return demandeStageService.getDemandeStageById(id);
    }
    @GetMapping("/offre/{offreId}")
    public List<DemandeStage> getDemandeStageByOffreId(@PathVariable Long offreId) {
        return demandeStageService.getDemandeStageByOffreId(offreId);
    }
    // ✅ Ajouter une demande de stage par ID de candidat
  /*  @PostMapping("/save/{candidatId}/{offreId}")
    public ResponseEntity<?> saveDemandeStage(
            @PathVariable Long candidatId,
            @PathVariable Long offreId,
            @RequestPart MultipartFile cv,
            @RequestPart MultipartFile lettreMotivation) {

        try {
            DemandeStage savedDemandeStage = demandeStageService.saveDemandeStage(candidatId, offreId, cv, lettreMotivation);

            // 🔔 Créer une notification pour le recruteur
            if (savedDemandeStage != null && savedDemandeStage.getOffre() != null) {
                Notification notification = new Notification();
                notification.setMessage("Un candidat a postulé à votre offre de stage.");
                notification.setRecruteur(savedDemandeStage.getOffre().getRecruteur());
                notification.setType("POSTULATION_STAGE");
                notification.setLu(false);
                notificationService.envoyerNotification(notification);
            }

            return ResponseEntity.ok(savedDemandeStage);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Le candidat a déjà postulé à cette offre.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du traitement du fichier : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }*/
    @PostMapping("/save/{candidatId}/{offreId}")
    public ResponseEntity<?> saveDemandeStage(
            @PathVariable Long candidatId,
            @PathVariable Long offreId,
            @RequestParam("cv") MultipartFile cv,
            @RequestParam("lettreMotivation") MultipartFile lettreMotivation) {
        try {
            DemandeStage savedDemandeStage = demandeStageService.saveDemandeStage(candidatId, offreId, cv, lettreMotivation);

            // 🔔 Créer une notification pour le recruteur
            if (savedDemandeStage != null && savedDemandeStage.getOffre() != null) {
                Notification notification = new Notification();
                notification.setMessage("Un candidat a postulé à votre offre de stage.");
                notification.setRecruteur(savedDemandeStage.getOffre().getRecruteur());
                notification.setType("POSTULATION_STAGE");
                notification.setLu(false);
                notificationService.envoyerNotification(notification);
            }

            return ResponseEntity.ok(savedDemandeStage);

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Le candidat a déjà postulé à cette offre.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du traitement du fichier : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }


    // ✅ Mettre à jour une demande de stage
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDemande(
            @PathVariable Long id,
            @RequestParam(value = "cv", required = false) MultipartFile cv,
            @RequestParam(value = "lettre_motivation", required = false) MultipartFile lettre_motivation) {
        try {
            DemandeStage updatedDemande = demandeStageService.updateDemandeStage(id, cv, lettre_motivation);
            return ResponseEntity.ok(updatedDemande);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour : " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Demande non trouvée : " + e.getMessage());
        }
    }

    // ✅ Supprimer une demande de stage par ID
    @DeleteMapping("/delete/{id}")
    public void deleteDemandeStage(@PathVariable Long id) {
        demandeStageService.deleteDemandeStage(id);
    }

    @PutMapping("/traiter/{id}")
    public ResponseEntity<Void> traiterDemande(@PathVariable Long id, @RequestParam boolean accepte) {
        demandeStageService.traiterDemande(id, accepte);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/by-candidat/{id}")
    public List<DemandeStage> getByCandidat(@PathVariable Long id) {
        return demandeStageService.getCandidaturesByCandidat(id);
    }
}




