package com.iset.plateformerecrutement.service;

import com.iset.plateformerecrutement.model.Candidat;
import com.iset.plateformerecrutement.model.DemandeStage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DemandeService {

    List<DemandeStage> getDemandesByRecruteurId(Long recruteurId);
    List<DemandeStage> getAllDemandeStage();
    Optional<DemandeStage> getDemandeStageById(Long id);
    DemandeStage saveDemandeStage(Long candidatId, Long offreId, MultipartFile cv, MultipartFile lettreMotivation) throws IOException  ;
    List<DemandeStage> getDemandeStageByCandidatId(Long candidatId);
    void deleteDemandeStage(Long id);
    DemandeStage updateDemandeStage(Long id, MultipartFile cv, MultipartFile lettre_motivation) throws IOException ;
    List<DemandeStage> getDemandeStageByOffreId(Long offreId);
    public List<DemandeStage> getCandidaturesByCandidat(Long idCandidat) ;
}
