package com.iset.plateformerecrutement.service;

import com.iset.plateformerecrutement.model.DemandeStage;
import com.iset.plateformerecrutement.model.Formation;
import com.iset.plateformerecrutement.requests.Formation_Request;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public interface FormationService {
    List<Formation> getFormationByFormateurId(Long formateurId);
    List<Formation> getAllFormation();
    Optional<Formation> getFormationById(Long id);
    Formation saveFormation(Long formateurId,  Formation_Request request, MultipartFile image) throws IOException;
    void deleteFormation(Long id);
    Formation updateFormation(Long formationId, Formation_Request request,MultipartFile image) throws IOException;
    void incrementerVues(Long id);
    Formation createFormation(Formation_Request formationRequest, MultipartFile imageFile) throws IOException;
}
