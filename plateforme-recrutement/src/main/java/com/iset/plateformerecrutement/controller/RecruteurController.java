package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.RecruteurServiceImpl;
import com.iset.plateformerecrutement.model.Recruteur;
import com.iset.plateformerecrutement.requests.RecruteurRegisterRequest;
import com.iset.plateformerecrutement.requests.RecruteurRequest;
import com.iset.plateformerecrutement.requests.SuccessMessageRequest;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/recruteurs")
public class RecruteurController {

    private final RecruteurServiceImpl recruteurService;

    @Autowired
    public RecruteurController(RecruteurServiceImpl recruteurService) {
        this.recruteurService = recruteurService;
    }

    // ✅ Récupérer tous les recruteurs
    @GetMapping("/all")
    public List<Recruteur> getAllRecruteurs() {
        return recruteurService.getAllRecruteur();
    }

    @PutMapping("/{id}/enable")
    public ResponseEntity<Map<String, String>> enableRecruteur(@PathVariable Long id) {
        recruteurService.enableRecruteur(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Recruteur activé avec succès.");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Map<String, String>> disableRecruteur(@PathVariable Long id) {
        recruteurService.disableRecruteur(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Recruteur désactivé avec succès.");
        return ResponseEntity.ok(response);
    }

    // ✅ Récupérer un recruteur par ID
    @GetMapping("/{id}")
    public ResponseEntity<Recruteur> getRecruteurById(@PathVariable Long id) {
        Optional<Recruteur> optionalRecruteur = recruteurService.getRecruteurById(id);

        if (optionalRecruteur.isPresent()) {
            Recruteur recruteur = optionalRecruteur.get();

            // 🚀 Force le chargement des offres publiées
            Hibernate.initialize(recruteur.getOffresPubliees());

            return ResponseEntity.ok(recruteur);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ✅ Ajouter un nouveau recruteur
    @PostMapping("/save")
    public Recruteur saveRecruteur(@RequestBody Recruteur recruteur) {
        return recruteurService.saveRecruteur(recruteur);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRecruteur(
            @PathVariable Long id,
            @ModelAttribute RecruteurRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Recruteur updatedRecruteur = recruteurService.updateRecruteur(id, request, image);
            return ResponseEntity.ok(updatedRecruteur);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour : " + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recruteur non trouvé : " + e.getMessage());
        }
    }



    // ✅ Supprimer un recruteur par ID
    @DeleteMapping("/delete/{id}")
    public void deleteRecruteur(@PathVariable Long id) {
        recruteurService.deleteRecruteur(id);
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerRecruteur(@RequestBody RecruteurRegisterRequest recruteurRegisterRequest) {
        try {
            recruteurService.registerRecruteur(recruteurRegisterRequest);
            return ResponseEntity.ok(new SuccessMessageRequest("Recruteur registered successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
