package com.iset.plateformerecrutement.service;

import com.iset.plateformerecrutement.model.Recruteur;
import com.iset.plateformerecrutement.requests.RecruteurRegisterRequest;
import com.iset.plateformerecrutement.requests.RecruteurRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface RecruteurService {
    List<Recruteur> getAllRecruteur();

    Optional<Recruteur> getRecruteurById(Long id);

    Recruteur saveRecruteur(Recruteur recruteur);

    void deleteRecruteur(Long id);

    Recruteur updateRecruteur(Long id, RecruteurRequest request, MultipartFile image) throws  IOException;
    void registerRecruteur(RecruteurRegisterRequest recruteurRegisterRequest);
     void disableRecruteur(Long id);
    void enableRecruteur(Long id);

}
