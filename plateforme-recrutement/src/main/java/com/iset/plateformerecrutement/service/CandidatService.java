package com.iset.plateformerecrutement.service;

import com.iset.plateformerecrutement.model.Candidat;

import com.iset.plateformerecrutement.requests.CandidatRequest;
import com.iset.plateformerecrutement.requests.CandidatUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CandidatService {
    List<Candidat> getAllCandidat();

    Candidat getcandidatById(Long id);
    Candidat savecandidat(Candidat candidat);

    void deletecandidat(Long id);

    Candidat updateCandidat(Long id, CandidatUpdateRequest request, MultipartFile image) throws IOException;

    Candidat registerCondidat(CandidatRequest candidatRequest);

}
