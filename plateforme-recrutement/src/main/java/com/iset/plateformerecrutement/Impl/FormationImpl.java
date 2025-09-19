package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.Candidat;
import com.iset.plateformerecrutement.model.Formation;
import com.iset.plateformerecrutement.model.Recruteur;
import com.iset.plateformerecrutement.repository.FormateurRepository;
import com.iset.plateformerecrutement.repository.FormationRepository;
import com.iset.plateformerecrutement.requests.Formation_Request;
import com.iset.plateformerecrutement.service.FormationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Format;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FormationImpl implements FormationService {

    private final FormationRepository formationrepository;
    private final FormateurRepository formateurrepository;
    private final StorageService storageService;

    @Override
    public List<Formation> getFormationByFormateurId(Long formateurId) {
        return formationrepository.findFormationsByFormateurId(formateurId);
    }

    @Override
    public List<Formation> getAllFormation() {
        return formationrepository.findAll();
    }
    @Override
    public Formation createFormation(Formation_Request formationRequest, MultipartFile imageFile) throws IOException {
        Formation formation = new Formation();

        // Mapping des données du DTO vers l'entité Formation
        formation.setTitre(formationRequest.getTitre());
        formation.setDescription(formationRequest.getDescription());
        formation.setCategorie(formationRequest.getCategorie());
        formation.setPrix(formationRequest.getPrix());
        formation.setDateDebut(formationRequest.getDateDebut());
        formation.setDateFin(formationRequest.getDateFin());
        formation.setPrerequis(formationRequest.getPrerequis());
        formation.setProgramme(formationRequest.getProgramme());
        formation.setNbre_participant(formationRequest.getNbre_participant());
        formation.setCertification(formationRequest.getCertification());
        formation.setDuree(formationRequest.getDuree());

        // Gestion de l'image
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            storageService.store(imageFile, fileName); // Stockage du fichier
            formation.setImage(fileName); // On sauvegarde juste le nom du fichier
        }

        // Sauvegarder la formation dans la base de données
        return formationrepository.save(formation);
    }
    @Override
    public Optional<Formation> getFormationById(Long id) {
        return formationrepository.findById(id);
    }
    public void incrementerVues(Long id) {
        Formation formation = formationrepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée avec l'id " + id));

        formation.setNbVues(formation.getNbVues() + 1);
        formationrepository.save(formation);
    }
    @Override
    public Formation saveFormation(Long formateurId,  Formation_Request detail, MultipartFile image) {
        return formateurrepository.findById(formateurId).map(formateur -> {
            try {
                Formation formation = new Formation();
                formation.setTitre(detail.getTitre());
                formation.setDescription(detail.getDescription());
                formation.setCategorie(detail.getCategorie());
                formation.setPrix(detail.getPrix());
                formation.setDateDebut(detail.getDateDebut());
                formation.setDateFin(detail.getDateFin());
                formation.setPrerequis(detail.getPrerequis());
                formation.setProgramme(detail.getProgramme());
                formation.setNbre_participant(detail.getNbre_participant());
                formation.setCertification(detail.getCertification());
                formation.setDuree(detail.getDuree());
                formation.setFormateur(formateur);

                if (image != null && !image.isEmpty()) {
                    String imageName = "formation_" + formateurId + "_" + image.getOriginalFilename();

                    if (!imageName.equals(formation.getImage())) {
                        if (formation.getImage() != null) {
                            Path oldImagePath = Paths.get("upload-dir").resolve(formation.getImage());
                            Files.deleteIfExists(oldImagePath);
                        }

                        storageService.store(image, imageName);
                        formation.setImage(imageName);
                    }
                }

                return formationrepository.save(formation);

            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de l’enregistrement de la formation avec image", e);
            }
        }).orElseThrow(() -> new RuntimeException("Formateur introuvable"));
    }

    @Override
    public void deleteFormation(Long id) {
        formationrepository.deleteById(id);
    }

    @Override
    public Formation updateFormation(Long formationId, Formation_Request request, MultipartFile image) {
        return formationrepository.findById(formationId).map(existingFormation -> {
            try {
                if (request.getTitre() != null) {
                    existingFormation.setTitre(request.getTitre());
                }
                if (request.getDescription() != null) {
                    existingFormation.setDescription(request.getDescription());
                }
                if (request.getCategorie() != null) {
                    existingFormation.setCategorie(request.getCategorie());
                }

                if (request.getPrix() != null) {
                    existingFormation.setPrix(request.getPrix());
                }
                if (request.getDateDebut() != null) {
                    existingFormation.setDateDebut(request.getDateDebut());
                }

                if (request.getDateFin() != null) {
                    existingFormation.setDateFin(request.getDateFin());
                }
                if (request.getPrerequis() != null) {
                    existingFormation.setPrerequis(request.getPrerequis());
                }
                if (request.getProgramme() != null) {
                    existingFormation.setProgramme(request.getProgramme());
                }
                if (request.getNbre_participant() != null) {
                    existingFormation.setNbre_participant(request.getNbre_participant());
                }
                if (request.getCertification() != null) {
                    existingFormation.setCertification(request.getCertification());
                }
                if (request.getDuree() != null) {
                    existingFormation.setDuree(request.getDuree());
                }

                if (image != null && !image.isEmpty()) {
                    String imageName = "formation_" + formationId + "_" + image.getOriginalFilename();

                    if (!imageName.equals(existingFormation.getImage())) {
                        if (existingFormation.getImage() != null) {
                            Path oldImagePath = Paths.get("upload-dir").resolve(existingFormation.getImage());
                            Files.deleteIfExists(oldImagePath);
                        }

                        storageService.store(image, imageName);
                        existingFormation.setImage(imageName);
                    }
                }

                return formationrepository.save(existingFormation);

            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la mise à jour de la formation avec image", e);
            }
        }).orElseThrow(() -> new RuntimeException("Formation introuvable"));
    }


}
