package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.CandidatServiceImpl;
import com.iset.plateformerecrutement.model.Candidat;
import com.iset.plateformerecrutement.model.Recruteur;
import com.iset.plateformerecrutement.requests.CandidatRequest;
import com.iset.plateformerecrutement.requests.CandidatUpdateRequest;
import com.iset.plateformerecrutement.requests.SuccessMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/candidat")
public class CandidatController {

    private final CandidatServiceImpl candidatService;

    @Autowired
    public CandidatController(CandidatServiceImpl candidatService) {
        this.candidatService = candidatService;
    }

    // ✅ Récupérer tous les candidats
    @GetMapping("/all")
    public List<Candidat> getAllCandidats() {
        return candidatService.getAllCandidat();
    }

    // ✅ Récupérer un candidat par ID
    @GetMapping("/{id}")
    public Candidat getCandidatById(@PathVariable Long id) {
        return candidatService.getcandidatById(id);
    }

    // ✅ Supprimer un candidat par ID
    @DeleteMapping("/delete/{id}")
    public void deleteCandidat(@PathVariable Long id) {
        candidatService.deletecandidat(id);
    }

    // ✅ Ajouter un candidat
    @PostMapping("/save")
    public Candidat saveCandidat(@RequestBody Candidat candidat) {
        return candidatService.savecandidat(candidat);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCandidat(
            @PathVariable Long id,
            @ModelAttribute CandidatUpdateRequest request,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Candidat updatedCandidat= candidatService.updateCandidat(id, request, image);
            return ResponseEntity.ok(updatedCandidat);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour : " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Recruteur non trouvé : " + e.getMessage());
        }
    }


    @PostMapping("/auth/register")
    public ResponseEntity<?> registerCondidat(@RequestBody CandidatRequest candidatRequest) {
        try {
            if (candidatRequest.getUsername() == null || candidatRequest.getUsername().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le champ username ne peut pas être vide !");
            }

            candidatService.registerCondidat(candidatRequest);
            return ResponseEntity.ok(new SuccessMessageRequest("Candidat enregistré avec succès !"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
