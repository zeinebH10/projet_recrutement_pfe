package com.iset.plateformerecrutement.service;

import com.iset.plateformerecrutement.model.Formateur;
import com.iset.plateformerecrutement.requests.Formateur_Register_Request;
import com.iset.plateformerecrutement.requests.Formateur_Request;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface FormateurService {
     List<Formateur> getAllFormateur();
     Optional<Formateur> getformateurById(Long id);
     void deleteFormateur(Long id);
     Formateur updateFormateur(Long id, Formateur_Request request, MultipartFile image) throws IOException;
    void registerFormateur(Formateur_Register_Request request);
    void enableFormateur(Long id);
    void disableFormateur(Long id);

}
