
package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.*;
import com.iset.plateformerecrutement.repository.CandidatRepository;
import com.iset.plateformerecrutement.repository.DemandeStageRepository;
import com.iset.plateformerecrutement.repository.NotificationRepository;
import com.iset.plateformerecrutement.repository.OffreRepository;
import com.iset.plateformerecrutement.service.DemandeService;
import com.iset.plateformerecrutement.service.EmailService;
import com.iset.plateformerecrutement.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DemandeStageServiceImpl implements DemandeService {
    @Autowired
    private final DemandeStageRepository demandeStageRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private final CandidatRepository candidatRepository;
    private final StorageService storageService;
    private final OffreRepository offreRepository;
    private final NotificationRepository notificationRepository;
    private NotificationService notificationService;

    public DemandeStageServiceImpl(DemandeStageRepository demandeStageRepository, CandidatRepository candidatRepository, StorageService storageService, OffreRepository offreRepository, NotificationRepository notificationRepository) {
        this.demandeStageRepository = demandeStageRepository;
        this.candidatRepository = candidatRepository;
        this.storageService = storageService;
        this.offreRepository = offreRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<DemandeStage> getDemandesByRecruteurId(Long recruteurId) {
        return demandeStageRepository.findByRecruteurId(recruteurId);
    }
    public List<DemandeStage> getAllDemandeStage() {
        return demandeStageRepository.findAll();
    }

    public Optional<DemandeStage> getDemandeStageById(Long id) {
        return demandeStageRepository.findById(id);
    }

 /*  public DemandeStage saveDemandeStage(Long candidatId, Long offreId, MultipartFile cv, MultipartFile lettreMotivation) throws IOException {
        Optional<DemandeStage> existingDemande = demandeStageRepository.findByCandidatIdAndOffreId(candidatId, offreId);
        if (existingDemande.isPresent()) {
            throw new IllegalStateException("Le candidat a déjà postulé à cette offre.");
        }

        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable avec l'ID : " + candidatId));

        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new EntityNotFoundException("Offre de stage introuvable avec l'ID : " + offreId));

        DemandeStage demandeStage = new DemandeStage();
        demandeStage.setCandidat(candidat);
        demandeStage.setOffre(offre);
        demandeStage.setStatut(StatutCandidature.EN_ATTENTE);

        if (cv != null && !cv.isEmpty()) {
            String cvName = "demande_" + candidatId + "_" + cv.getOriginalFilename();
            storageService.store(cv, cvName);
            demandeStage.setCv(cvName);
        }
        if (lettreMotivation != null && !lettreMotivation.isEmpty()) {
            String lettreName = "demande_" + candidatId + "_" + lettreMotivation.getOriginalFilename();
            storageService.store(lettreMotivation, lettreName);
            demandeStage.setLettre_motivation(lettreName);
        }

        return demandeStageRepository.save(demandeStage);
    }
*/


    public List<DemandeStage> getDemandeStageByCandidatId(Long candidatId) {
        return demandeStageRepository.findByCandidatId(candidatId);
    }
    public List<DemandeStage> getDemandeStageByOffreId(Long offreId) {
        return demandeStageRepository.findByOffreStageId(offreId);
    }

    @Override
    public List<DemandeStage> getCandidaturesByCandidat(Long idCandidat) {
        return demandeStageRepository.findByCandidatId(idCandidat);
    }

    public void deleteDemandeStage(Long id) {
        demandeStageRepository.deleteById(id);
    }
    @Override
    public DemandeStage saveDemandeStage(Long candidatId, Long offreId, MultipartFile cv, MultipartFile lettreMotivation) throws IOException {
        Optional<DemandeStage> existingDemande = demandeStageRepository.findByCandidatIdAndOffreId(candidatId, offreId);
        if (existingDemande.isPresent()) {
            throw new IllegalStateException("Le candidat a déjà postulé à cette offre.");
        }

        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable avec l'ID : " + candidatId));

        Offre offre = offreRepository.findById(offreId)
                .orElseThrow(() -> new EntityNotFoundException("Offre de stage introuvable avec l'ID : " + offreId));

        DemandeStage demandeStage = new DemandeStage();
        demandeStage.setCandidat(candidat);
        demandeStage.setOffre(offre);
        demandeStage.setStatut(StatutCandidature.EN_ATTENTE);

        if (cv != null && !cv.isEmpty()) {
            String cvName = "demande_" + candidatId + "_" + cv.getOriginalFilename();
            storageService.store(cv, cvName);
            demandeStage.setCv(cvName);
        }

        if (lettreMotivation != null && !lettreMotivation.isEmpty()) {
            String lettreName = "demande_" + candidatId + "_" + lettreMotivation.getOriginalFilename();
            storageService.store(lettreMotivation, lettreName);
            demandeStage.setLettre_motivation(lettreName);
        }

        DemandeStage savedDemande = demandeStageRepository.save(demandeStage);

        // ✅ Notification au recruteur
        Recruteur recruteur = offre.getRecruteur();
        Notification notification = new Notification();
        notification.setRecruteur(recruteur);
        notification.setMessage(candidat.getUsername()+ "a postulé à votre offre : " + offre.getTitre());
        notification.setType("POSTULATION_STAGE");
        notification.setLu(false);
        notificationRepository.save(notification);

        // ✅ Envoi email au recruteur
        try {
            emailService.envoyerEmail(
                    recruteur.getEmail(),
                    "Nouvelle candidature pour votre offre",
                    "Bonjour " + recruteur.getFullName() + ",\n\n" + candidat.getFullName() +" a postulé à votre offre : \"" + offre.getTitre() + "\".\nConnectez-vous pour consulter sa candidature."
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email au recruteur : " + e.getMessage());
        }

        return savedDemande;
    }

    public DemandeStage updateDemandeStage(Long id, MultipartFile cv, MultipartFile lettre_motivation) throws IOException {
        Optional<DemandeStage> optionalDemandeStage = demandeStageRepository.findById(id);
        if (optionalDemandeStage.isPresent()) {
            DemandeStage existingDemandeStage = optionalDemandeStage.get();
            if (cv != null && !cv.isEmpty()) {
                String cvName = "cv_" + id + "_" + cv.getOriginalFilename();
                if (!cvName.equals(existingDemandeStage.getCv())) {
                    if (existingDemandeStage.getCv() != null) {
                        Path oldCvPath = Paths.get("upload-dir").resolve(existingDemandeStage.getCv());
                        Files.deleteIfExists(oldCvPath);  // Delete old CV
                    }
                    storageService.store(cv, cvName);
                    existingDemandeStage.setCv(cvName);
                }
            }
            if (lettre_motivation != null && !lettre_motivation.isEmpty()) {
                String lettreName = "lettre_" + id + "_" + lettre_motivation.getOriginalFilename();
                if (!lettreName.equals(existingDemandeStage.getLettre_motivation())) {
                    if (existingDemandeStage.getLettre_motivation() != null) {
                        Path oldLettrePath = Paths.get("upload-dir").resolve(existingDemandeStage.getLettre_motivation());
                        Files.deleteIfExists(oldLettrePath);
                    }
                    storageService.store(lettre_motivation, lettreName);
                    existingDemandeStage.setLettre_motivation(lettreName);  // Update the Lettre name
                }
            }
            return demandeStageRepository.save(existingDemandeStage);
        } else {
            throw new EntityNotFoundException("Demande introuvable avec l'ID : " + id);
        }
    }


    public void traiterDemande(Long demandeId, boolean accepte) {
        DemandeStage demande = demandeStageRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        demande.setStatut(accepte ? StatutCandidature.ACCEPTEE : StatutCandidature.REFUSEE);
        demandeStageRepository.save(demande);

        Candidat candidat = demande.getCandidat();
        Notification notification = new Notification();
        notification.setCandidat(candidat);
        notification.setType(accepte ? "ACCEPTEE" : "REFUSEE");
        notification.setLu(false);

        String message;
        if (accepte) {
            message = "Félicitations ! Vous êtes invité à un rendez-vous pour le stage.";
        } else {
            message = "Nous sommes désolés. Votre demande de stage n'a pas été acceptée.";
        }

        notification.setMessage(message);
        notificationRepository.save(notification);

        // ✅ Envoyer email
        emailService.envoyerEmail(
                candidat.getEmail(),
                "Réponse à votre candidature de stage",
                message
        );
    }


}