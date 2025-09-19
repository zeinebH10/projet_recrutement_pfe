package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.Candidat;
import com.iset.plateformerecrutement.model._Role;
import com.iset.plateformerecrutement.repository.CandidatRepository;
import com.iset.plateformerecrutement.requests.CandidatRequest;
import com.iset.plateformerecrutement.requests.CandidatUpdateRequest;
import com.iset.plateformerecrutement.service.CandidatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class CandidatServiceImpl implements CandidatService {

    private final CandidatRepository candidatRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    public CandidatServiceImpl(CandidatRepository candidatRepository, PasswordEncoder passwordEncoder, StorageService storageService) {
        this.candidatRepository = candidatRepository;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
    }

    @Override
    public List<Candidat> getAllCandidat() {
        return candidatRepository.findAll();
    }

    @Override
    public Candidat getcandidatById(Long id) {
        return candidatRepository.findById(id).get();
    }

    @Override
    public Candidat savecandidat(Candidat candidat) {
        return candidatRepository.save(candidat);
    }

    @Override
    public void deletecandidat(Long id) {
        candidatRepository.deleteById(id);
    }

    @Override
    public Candidat updateCandidat(Long id, CandidatUpdateRequest request, MultipartFile image) throws IOException {
        Optional<Candidat> optionalCandidat = candidatRepository.findById(id);

        if (optionalCandidat.isPresent()) {
            Candidat existingCandidat = optionalCandidat.get();

            if (request.getFullName() != null) {
                existingCandidat.setFullName(request.getFullName());
            }

            if (request.getBirthDate() != null) {
                existingCandidat.setBirthDate(request.getBirthDate());
            }

            if (request.getNiveauEtude() != null) {
                existingCandidat.setNiveauEtude(request.getNiveauEtude());
            }

            if (request.getCompetences() != null) {
                existingCandidat.setCompetences(request.getCompetences());
            }
            if (request.getAdresse() != null) {
                existingCandidat.setAdresse(request.getAdresse());
            }
            if (request.getTelephone()!= null) {
                existingCandidat.setTelephone(request.getTelephone());
            }
            if (request.getCompteLinkedin()!= null) {
                existingCandidat.setCompteLinkedin(request.getCompteLinkedin());
            }
            if (request.getExperience()!= null) {
                existingCandidat.setExperience(request.getExperience());
            }
            if (request.getLangue()!= null) {
                existingCandidat.setLangue(request.getLangue());
            }
            if (image != null && !image.isEmpty()) {
                String imageName = "candidat_" + id + "_" + image.getOriginalFilename();

                // Vérifier si l'image est identique à l'existante
                if (!imageName.equals(existingCandidat.getImage())) {
                    // Supprimer l'ancienne image si elle existe
                    if (existingCandidat.getImage() != null) {
                        Path oldImagePath = Paths.get("upload-dir").resolve(existingCandidat.getImage());
                        Files.deleteIfExists(oldImagePath);
                    }

                    storageService.store(image, imageName);
                    existingCandidat.setImage(imageName);
                }
            }
            return candidatRepository.save(existingCandidat);
        } else {
            throw new RuntimeException("Candidat introuvable avec l'ID : " + id);
        }
    }

    @Override
    public Candidat registerCondidat(CandidatRequest candidatRequest) {
        Optional<Candidat> existingUserByEmail = candidatRepository.findByEmail(candidatRequest.getEmail());
        Optional<Candidat> existingUserByUsername = candidatRepository.findByUsername(candidatRequest.getUsername());

        if (existingUserByEmail.isPresent() || existingUserByUsername.isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email ou nom d'utilisateur existe déjà !");
        }

        Candidat candidat = Candidat.builder()
                .fullName(candidatRequest.getFullName())
                .email(candidatRequest.getEmail())
                .birthDate(candidatRequest.getBirthDate())
                .username(candidatRequest.getUsername())
                .password(passwordEncoder.encode(candidatRequest.getPassword()))
                .isEnabled(true)
                .role(_Role.ROLE_CANDIDAT)
                .niveauEtude(candidatRequest.getNiveauEtude())
                .telephone(candidatRequest.getTelephone())
                .build();


        Candidat savedCandidat = candidatRepository.save(candidat);
        System.out.println("Candidat après enregistrement : " + savedCandidat);

        return savedCandidat;
    }
}
