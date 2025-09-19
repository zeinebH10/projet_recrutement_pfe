package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.Formation;
import com.iset.plateformerecrutement.model.Offre;
import com.iset.plateformerecrutement.model.Recruteur;
import com.iset.plateformerecrutement.repository.OffreRepository;
import com.iset.plateformerecrutement.repository.RecruteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OffreServiceImpl {
    private final OffreRepository offreRepository;
    private final RecruteurRepository recruteurRepository;

    @Autowired
    public OffreServiceImpl(OffreRepository offreRepository, RecruteurRepository recruteurRepository) {
        this.offreRepository = offreRepository;
        this.recruteurRepository = recruteurRepository;
    }
    public long getTotalNumberOfOffers() {
        return offreRepository.count();
    }
    public List<Offre> getAllOffre() {
        return offreRepository.findAll();
    }


    public Offre saveOffre(Offre offre) {
        return offreRepository.save(offre);
    }
    public Optional<Offre> getOffreById(Long id) {
        return offreRepository.findById(id);
    }

    public List<Offre> getOffresByRecruteur(Long recruteurId) {
        return offreRepository.findByRecruteurId(recruteurId);
    }

    public Offre addOffreByRecruteur(Long recruteurId, Offre offre) {
        Optional<Recruteur> recruteurOpt = recruteurRepository.findById(recruteurId);
        if (recruteurOpt.isEmpty()) {
            throw new RuntimeException("Recruteur non trouvé pour l'ID: " + recruteurId);
        }

        Recruteur recruteur = recruteurOpt.get();
        offre.setRecruteur(recruteur);

        // Vérification que la date de fin est après la date de début
        if (offre.getDateDebut() != null && offre.getDateFin() != null &&
                offre.getDateFin().isBefore(offre.getDateDebut())) {
            throw new RuntimeException("La date de fin doit être postérieure à la date de début.");
        }

        return offreRepository.save(offre);
    }

    public Offre updateOffre(Long id, Offre offreDetails) {
        Offre offre = offreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offre non trouvée pour l'ID: " + id));

        // Mettez à jour uniquement les champs non nulls
        if (offreDetails.getTitre() != null) {
            offre.setTitre(offreDetails.getTitre());
        }
        if (offreDetails.getDescription() != null) {
            offre.setDescription(offreDetails.getDescription());
        }
        if (offreDetails.getType() != null) {
            offre.setType(offreDetails.getType());
        }
        if (offreDetails.getDomaine() != null) {
            offre.setDomaine(offreDetails.getDomaine());
        }
        if (offreDetails.getTeletravail() != null) {
            offre.setTeletravail(offreDetails.getTeletravail());
        }
        if (offreDetails.getLocalisation() != null) {
            offre.setLocalisation(offreDetails.getLocalisation());
        }
        if (offreDetails.getTechnologies() != null) {
            offre.setTechnologies(offreDetails.getTechnologies());
        }
        if (offreDetails.getDateDebut() != null) {
            offre.setDateDebut(offreDetails.getDateDebut());
        }
        if (offreDetails.getDateFin() != null) {
            offre.setDateFin(offreDetails.getDateFin());
        }
        if (offreDetails.getNombreStagiaire() != 0) {
            offre.setNombreStagiaire(offreDetails.getNombreStagiaire());
        }
        if (offreDetails.getCompetences() != null) {
            offre.setCompetences(offreDetails.getCompetences());
        }
        if (offreDetails.getExperience() != null) {
            offre.setExperience(offreDetails.getExperience());
        }
        if (offreDetails.getNiveauEtude() != null) {
            offre.setNiveauEtude(offreDetails.getNiveauEtude());
        }
        if (offreDetails.getLangue() != null) {
            offre.setLangue(offreDetails.getLangue());
        }
        if (offreDetails.isPayment() != null) {
            offre.setPayment(offreDetails.isPayment());
        }
        return offreRepository.save(offre);
    }


    public void deleteOffre(Long id) {
        offreRepository.deleteById(id);
    }


}