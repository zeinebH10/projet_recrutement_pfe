package com.iset.plateformerecrutement.service;

import com.iset.plateformerecrutement.model.DemandeFormation;
import com.iset.plateformerecrutement.requests.DemandeFormationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface DemandeFormationService {

    List<DemandeFormation> getDemandesByFormateurId(Long formateurId);
    List<DemandeFormation> getAllDemandeFormation();
    Optional<DemandeFormation> getDemandeFormationById(Long id);
    DemandeFormation saveDemandeFormation(Long candidatId, Long formationId,DemandeFormationRequest request);
    List<DemandeFormation> getDemandeFormationByCandidatId(Long candidatId);
    void deleteDemandeFormation(Long id);
    DemandeFormation updateDemandeFormation(Long id, DemandeFormationRequest request)  ;
    List<DemandeFormation> getDemandeFormationByFormationId(Long formationId);
    void traiterDemande(Long demandeId, boolean accepte);
}
