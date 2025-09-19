package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.FormateurServiceImpl;
import com.iset.plateformerecrutement.model.Formateur;
import com.iset.plateformerecrutement.requests.Formateur_Register_Request;
import com.iset.plateformerecrutement.requests.Formateur_Request;
import com.iset.plateformerecrutement.requests.SuccessMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/formateur")
@CrossOrigin(origins = "*") // Permet les requêtes depuis n'importe quelle origine (à ajuster selon besoin)
public class FormateurController {

    private final FormateurServiceImpl formateurService;

    public FormateurController(FormateurServiceImpl formateurService) {
        this.formateurService = formateurService;
    }

    @GetMapping("/all")
            public ResponseEntity<List<Formateur>> getAllFormateurs() {
                List<Formateur> formateurs = formateurService.getAllFormateur();
                return ResponseEntity.ok(formateurs);
            }

            // ✅ 2. Récupérer un formateur par ID
            @GetMapping("/{id}")
            public ResponseEntity<Formateur> getFormateurById(@PathVariable Long id) {
                Optional<Formateur> formateur = formateurService.getformateurById(id);
                return formateur.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
            }

            // ✅ 3. Supprimer un formateur
            @DeleteMapping("/delete/{id}")
            public ResponseEntity<Void> deleteFormateur(@PathVariable Long id) {
                formateurService.deleteFormateur(id);
                return ResponseEntity.noContent().build(); // 204 No Content après suppression
            }

            // ✅ 4. Mettre à jour un formateur
            @PutMapping("/update/{id}")
            public ResponseEntity<Formateur> updateFormateur(
                    @PathVariable Long id,
                    @ModelAttribute Formateur_Request request,
                    @RequestParam(value = "image", required = false) MultipartFile image) {

                try {
                    Formateur updatedFormateur = formateurService.updateFormateur(id, request, image);
                    return ResponseEntity.ok(updatedFormateur);
                } catch (IOException e) {
                    return ResponseEntity.internalServerError().build();
                } catch (RuntimeException e) {
                    return ResponseEntity.notFound().build();
                }
            }

            // ✅ 5. Enregistrer un nouveau formateur
            @PostMapping("/register")
            public ResponseEntity<?>registerFormateur(@RequestBody Formateur_Register_Request request) {
                try {
                    if (request.getUsername() == null || request.getUsername().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le champ username ne peut pas être vide !");
                    }
                    if (request.getPassword() == null || request.getPassword().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le champ username ne peut pas être vide !");
                    }
            formateurService.registerFormateur(request);
            return ResponseEntity.ok(new SuccessMessageRequest("Formateur enregistré avec succès !"));

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Map<String, String>> enableFormateur(@PathVariable Long id) {
        formateurService.enableFormateur(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Formateur activé avec succès.");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Map<String, String>> disableFormateur(@PathVariable Long id) {
        formateurService.disableFormateur(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Formateur désactivé avec succès.");
        return ResponseEntity.ok(response);
    }
}
