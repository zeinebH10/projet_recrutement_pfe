package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.Recruteur;
import com.iset.plateformerecrutement.model._Role;
import com.iset.plateformerecrutement.repository.RecruteurRepository;
import com.iset.plateformerecrutement.requests.RecruteurRegisterRequest;
import com.iset.plateformerecrutement.requests.RecruteurRequest;
import com.iset.plateformerecrutement.service.RecruteurService;
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
public class RecruteurServiceImpl implements RecruteurService {
    private final RecruteurRepository recruteurRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;

    @Autowired
    public RecruteurServiceImpl(RecruteurRepository recruteurRepository, PasswordEncoder passwordEncoder, StorageService storageService) {
        this.recruteurRepository = recruteurRepository;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
    }

    @Override
    public List<Recruteur> getAllRecruteur() {
        return recruteurRepository.findAll();
    }

    @Override
    public Optional<Recruteur> getRecruteurById(Long id) {
        return recruteurRepository.findById(id);
    }

    @Override
    public Recruteur saveRecruteur(Recruteur recruteur) {
        return recruteurRepository.save(recruteur);
    }

    @Override
    public void deleteRecruteur(Long id) {
        recruteurRepository.deleteById(id);
    }

    @Override
    public Recruteur updateRecruteur(Long id, RecruteurRequest request, MultipartFile image) throws IOException {
        Optional<Recruteur> existingRecruteurOpt = recruteurRepository.findById(id);
        if (existingRecruteurOpt.isEmpty()) {
            throw new RuntimeException("Recruteur non trouvé pour ID: " + id);
        }

        Recruteur recruteur = existingRecruteurOpt.get();

        // Mise à jour des champs non nulls
        if (request.getFullName() != null) {
            recruteur.setFullName(request.getFullName());
        }
        if (request.getBirthDate() != null) {
            recruteur.setBirthDate(request.getBirthDate());
        }
        if (request.getNomEntreprise() != null) {
            recruteur.setNomEntreprise(request.getNomEntreprise());
        }
        if (request.getDescription() != null) {
            recruteur.setDescription(request.getDescription());
        }
        if (request.getSpecialite() != null) {
            recruteur.setSpecialite(request.getSpecialite());
        }
        if (request.getSiteWeb() != null) {
            recruteur.setSiteWeb(request.getSiteWeb());
        }
        if (request.getTelephone() != null) {
            recruteur.setTelephone(request.getTelephone());
        }
        if (request.getCompteLinkedin() != null) {
            recruteur.setCompteLinkedin(request.getCompteLinkedin());
        }
        if (request.getAdresse() != null) {
            recruteur.setAdresse(request.getAdresse());
        }
        // Gestion de l'image (évite les erreurs 404 et les doublons)
        if (image != null && !image.isEmpty()) {
            String imageName = "recruteur_" + id + "_" + image.getOriginalFilename();

            // Vérifier si l'image est identique à l'existante
            if (!imageName.equals(recruteur.getImage())) {
                // Supprimer l'ancienne image si elle existe
                if (recruteur.getImage() != null) {
                    Path oldImagePath = Paths.get("upload-dir").resolve(recruteur.getImage());
                    Files.deleteIfExists(oldImagePath);
                }

                // Stocker la nouvelle image avec un nom unique si nécessaire
                storageService.store(image, imageName);
                recruteur.setImage(imageName);
            }
        }

        return recruteurRepository.save(recruteur);
    }



    @Override
    public void registerRecruteur(RecruteurRegisterRequest recruteurRegisterRequest) {
        Optional<Recruteur> existingUserByEmail = recruteurRepository.findByEmail(recruteurRegisterRequest.getEmail());
        Optional<Recruteur> existingUserByUsername = recruteurRepository.findByUsername(recruteurRegisterRequest.getUsername());

        if (existingUserByEmail.isPresent() || existingUserByUsername.isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email ou ce nom d'utilisateur existe déjà !");
        }

        Recruteur recruteur = Recruteur.builder()
                .fullName(recruteurRegisterRequest.getFullName())
                .email(recruteurRegisterRequest.getEmail())
                .username(recruteurRegisterRequest.getUsername())
                .birthDate(recruteurRegisterRequest.getBirthDate())
                .isEnabled(false)
                .role(_Role.ROLE_RH)
                .password(passwordEncoder.encode(recruteurRegisterRequest.getPassword()))
                .nomEntreprise(recruteurRegisterRequest.getNomEntreprise())
                .telephone(recruteurRegisterRequest.getTelephone())
                .build();

        // Sauvegarde du recruteur
        Recruteur savedRecruteur = recruteurRepository.save(recruteur);
        System.out.println("Recruteur après enregistrement : " + savedRecruteur);

        recruteurRepository.save(recruteur);
    }

    @Override
    public void enableRecruteur(Long id) {
        Optional<Recruteur> recruteurOpt = recruteurRepository.findById(id);
        if (recruteurOpt.isPresent()) {
            Recruteur recruteur = recruteurOpt.get();
            recruteur.setEnabled(true);
            recruteurRepository.save(recruteur);
        } else {
            throw new RuntimeException("Recruteur non trouvé pour ID: " + id);
        }
    }

    @Override
    public void disableRecruteur(Long id) {
        Optional<Recruteur> recruteurOpt = recruteurRepository.findById(id);
        if (recruteurOpt.isPresent()) {
            Recruteur recruteur = recruteurOpt.get();
            recruteur.setEnabled(false);
            recruteurRepository.save(recruteur);
        } else {
            throw new RuntimeException("Recruteur non trouvé pour ID: " + id);
        }
    }


}
