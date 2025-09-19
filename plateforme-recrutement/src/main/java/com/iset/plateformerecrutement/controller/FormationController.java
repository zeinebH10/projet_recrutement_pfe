package com.iset.plateformerecrutement.controller;


import com.iset.plateformerecrutement.model.Formation;
import com.iset.plateformerecrutement.model.Offre;
import com.iset.plateformerecrutement.requests.Formation_Request;
import com.iset.plateformerecrutement.service.FormationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/formation")
@CrossOrigin(origins = "*") // Permettre l'accès depuis n'importe quel frontend
public class FormationController {

    private final FormationService formationService;

    public FormationController(FormationService formationService) {
        this.formationService = formationService;
    }

    // Récupérer toutes les formations
    @GetMapping("/all")
    public ResponseEntity<List<Formation>> getAllFormations() {
        return ResponseEntity.ok(formationService.getAllFormation());
    }
    @PostMapping(value = "/creat", consumes = "multipart/form-data")
    public ResponseEntity<Formation> createFormation(
            @ModelAttribute Formation_Request formationRequest,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Formation savedFormation = formationService.createFormation(formationRequest, image);
            return new ResponseEntity<>(savedFormation, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Récupérer une formation par ID
    @GetMapping("/{id}")
    public ResponseEntity<Formation> getFormationById(@PathVariable Long id) {
        Optional<Formation> formation = formationService.getFormationById(id);
        return formation.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("/increment-vues/{id}")
    public ResponseEntity<?> incrementVues(@PathVariable Long id) {
        try {
            formationService.incrementerVues(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer les formations d'un formateur
    @GetMapping("/formateur/{formateurId}")
    public ResponseEntity<List<Formation>> getFormationByFormateur(@PathVariable Long formateurId) {
        return ResponseEntity.ok(formationService.getFormationByFormateurId(formateurId));
    }

    // Ajouter une formation (seulement par un formateur)
    @PostMapping("/add/{formateurId}")
    public ResponseEntity<Formation> saveFormation(
            @PathVariable Long formateurId,
            @ModelAttribute  Formation_Request request,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Formation savedFormation = formationService.saveFormation(formateurId, request, image);
            return ResponseEntity.ok(savedFormation);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Mettre à jour une formation
    @PutMapping("/update/{id}")
    public ResponseEntity<Formation> updateFormation(
            @PathVariable Long id,
            @ModelAttribute Formation_Request request,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Formation updatedFormation = formationService.updateFormation(id, request, image);
            return ResponseEntity.ok(updatedFormation);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une formation
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFormation(@PathVariable Long id) {
        formationService.deleteFormation(id);
        return ResponseEntity.noContent().build();
    }
}

