package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.CVService;
import com.iset.plateformerecrutement.requests.OffreDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200", "https://votre-domaine-production.com"})
@RestController
@RequestMapping("/api/cv")
@RequiredArgsConstructor
@Slf4j
public class CVController {

    private final CVService cvService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCV(@RequestParam("file") MultipartFile file) {
        // Validation du fichier
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Le fichier est vide.");
        }

        if (!file.getContentType().equalsIgnoreCase("application/pdf")) {
            return ResponseEntity.badRequest()
                    .body("Seuls les fichiers PDF sont acceptés. Type reçu: " + file.getContentType());
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB max
            return ResponseEntity.badRequest()
                    .body("La taille du fichier ne doit pas dépasser 5MB.");
        }

        try {
            String extractedText = cvService.extractTextFromPDF(file);
            log.debug("Texte extrait du CV (premieres 100 chars): {}", extractedText.substring(0, Math.min(100, extractedText.length())));

            List<String> detectedSkills = cvService.detectSkills(extractedText);
            log.info("Compétences détectées: {}", detectedSkills);

            List<OffreDTO> offresCompatibles = cvService.findMatchingOffers(detectedSkills);

            if (offresCompatibles.isEmpty()) {
                return ResponseEntity.ok().body("Aucune offre correspondante trouvée pour les compétences détectées.");
            }

            return ResponseEntity.ok(offresCompatibles);

        } catch (IOException e) {
            log.error("Erreur de lecture du fichier PDF", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la lecture du fichier PDF.");
        } catch (Exception e) {
            log.error("Erreur lors de l'analyse du CV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur inattendue est survenue lors du traitement du CV.");
        }
    }
}