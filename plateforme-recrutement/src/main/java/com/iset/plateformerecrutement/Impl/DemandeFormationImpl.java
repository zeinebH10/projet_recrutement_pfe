package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.*;
import com.iset.plateformerecrutement.repository.CandidatRepository;
import com.iset.plateformerecrutement.repository.DemandeFormationRepository;
import com.iset.plateformerecrutement.repository.FormationRepository;
import com.iset.plateformerecrutement.repository.NotificationRepository;
import com.iset.plateformerecrutement.requests.DemandeFormationRequest;
import com.iset.plateformerecrutement.service.DemandeFormationService;
import com.iset.plateformerecrutement.service.EmailService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class DemandeFormationImpl implements DemandeFormationService {
private final DemandeFormationRepository demandeformationrepository;
    private final CandidatRepository candidatRepository;
    private final FormationRepository formationRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    private EmailService emailService;
    public DemandeFormationImpl(DemandeFormationRepository demandeformationrepository, CandidatRepository candidatRepository, FormationRepository formationRepository, NotificationRepository notificationRepository) {
        this.demandeformationrepository = demandeformationrepository;
        this.candidatRepository = candidatRepository;
        this.formationRepository = formationRepository;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<DemandeFormation> getDemandesByFormateurId(Long formateurId) {
        return demandeformationrepository.findByFormateurId(formateurId);    }

    @Override
    public List<DemandeFormation> getAllDemandeFormation() {
        return demandeformationrepository.findAll();    }

    @Override
    public Optional<DemandeFormation> getDemandeFormationById(Long id) {
        return demandeformationrepository.findById(id);
    }

    /* @Override
   public DemandeFormation saveDemandeFormation(Long candidatId, Long formationId, DemandeFormationRequest request) {
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable avec l'ID : " + candidatId));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation introuvable avec l'ID : " + formationId));
        DemandeFormation demandeFormation = new DemandeFormation();
        demandeFormation.setCandidat(candidat);
        demandeFormation.setFormation(formation);
        demandeFormation.setMotivation(request.getMotivation());
        demandeFormation.setTeletravail(request.getTeletravail());
        demandeFormation.setMethode_apprentissage(request.getMethode_apprentissage());
        demandeFormation.setLangue(request.getLangue());
        return demandeformationrepository.save(demandeFormation);
    }*/
    @Override
    public DemandeFormation saveDemandeFormation(Long candidatId, Long formationId, DemandeFormationRequest request) {
        Candidat candidat = candidatRepository.findById(candidatId)
                .orElseThrow(() -> new EntityNotFoundException("Candidat introuvable avec l'ID : " + candidatId));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new EntityNotFoundException("Formation introuvable avec l'ID : " + formationId));

        DemandeFormation demandeFormation = new DemandeFormation();
        demandeFormation.setCandidat(candidat);
        demandeFormation.setFormation(formation);
        demandeFormation.setMotivation(request.getMotivation());
        demandeFormation.setTeletravail(request.getTeletravail());
        demandeFormation.setMethode_apprentissage(request.getMethode_apprentissage());
        demandeFormation.setLangue(request.getLangue());

        DemandeFormation savedDemande = demandeformationrepository.save(demandeFormation);

        // ✅ Notification au formateur
        Formateur formateur = formation.getFormateur();
        Notification notification = new Notification();
        notification.setFormateur(formateur);
        notification.setMessage("Un nouveau candidat a postulé à votre formation : " + formation.getTitre());
        notification.setType("POSTULATION_FORMATION");
        notification.setLu(false);
        notificationRepository.save(notification);

        // ✅ Envoi email au formateur
        try {
            emailService.envoyerEmail(
                    formateur.getEmail(),
                    "Nouvelle postulation à votre formation",
                    "Bonjour " + formateur.getFullName() + ",\n\n "+ candidat.getFullName() +" a postulé à votre formation : \"" + formation.getTitre() + "\".\nConnectez-vous pour consulter sa candidature."
            );
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email au formateur : " + e.getMessage());
        }

        return savedDemande;
    }

    @Override
    public DemandeFormation updateDemandeFormation(Long id, DemandeFormationRequest request) {
        DemandeFormation existingDemandeFormation = demandeformationrepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Demande introuvable avec l'ID : " + id));
        existingDemandeFormation.setMotivation(request.getMotivation());
        existingDemandeFormation.setTeletravail(request.getTeletravail());
        existingDemandeFormation.setMethode_apprentissage(request.getMethode_apprentissage());
        existingDemandeFormation.setLangue(request.getLangue());

        return demandeformationrepository.save(existingDemandeFormation);
    }

    @Override
    public List<DemandeFormation> getDemandeFormationByCandidatId(Long candidatId) {
        return demandeformationrepository.findByCandidatId(candidatId);
    }


    @Override
    public void deleteDemandeFormation(Long id) {
        demandeformationrepository.deleteById(id);
    }
    @Override
    public List<DemandeFormation> getDemandeFormationByFormationId(Long formationId){
        return demandeformationrepository.findByFormationId(formationId);
    }

  /*  public void traiterDemande(Long demandeId, boolean accepte) {
        DemandeFormation demande = demandeformationrepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
        Candidat candidat = demande.getCandidat();
        Notification notification = new Notification();
        notification.setCandidat(candidat);
        notification.setType(accepte ? "ACCEPTEE" : "REFUSEE");
        notification.setLu(false);

        if (accepte) {
            notification.setMessage(" Félicitations !\n" + "Votre demande de participation à la formation  a été acceptée.\n" +"Nous sommes ravis de vous compter parmi les participants. .");
        } else {
            notification.setMessage("❗ Votre demande de participation à la formation  a été refusée. Malheureusement, les places sont limitées");
        }

        notificationRepository.save(notification);
    }*/


 /*   public void traiterDemande(Long demandeId, boolean accepte) {
        DemandeFormation demande = demandeformationrepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
        Candidat candidat = demande.getCandidat();
        Notification notification = new Notification();
        notification.setCandidat(candidat);
        notification.setType(accepte ? "ACCEPTEE" : "REFUSEE");
        notification.setLu(false);

        String message;
        if (accepte) {
            message = "Félicitations !\nVotre demande de participation à la formation a été acceptée.";
        } else {
            message = "❗ Votre demande de participation à la formation a été refusée. Les places sont limitées.";
        }

        notification.setMessage(message);
        notificationRepository.save(notification);

        // ✅ Envoyer email
        emailService.envoyerEmail(
                candidat.getEmail(),
                "Réponse à votre demande de formation",
                message
        );
    }*/



    public void traiterDemande(Long demandeId, boolean accepte) {
        DemandeFormation demande = demandeformationrepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));
        Candidat candidat = demande.getCandidat();
        Notification notification = new Notification();
        notification.setCandidat(candidat);
        notification.setType(accepte ? "ACCEPTEE" : "REFUSEE");
        notification.setLu(false);

        String message;
        if (accepte) {
            message = "Félicitations !\nVotre demande de participation à la formation a été acceptée.";
        } else {
            message = "❗ Votre demande de participation à la formation a été refusée. Les places sont limitées.";
        }

        notification.setMessage(message);
        notificationRepository.save(notification);

        // ✅ Envoyer email sans bloquer
        try {
            emailService.envoyerEmail(
                    candidat.getEmail(),
                    "Réponse à votre demande de formation",
                    message
            );
        } catch (Exception e) {
            System.err.println("Erreur envoi email: " + e.getMessage());
        }
    }

}
