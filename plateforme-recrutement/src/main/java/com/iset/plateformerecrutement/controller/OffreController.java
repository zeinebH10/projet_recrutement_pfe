package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.OffreServiceImpl;
import com.iset.plateformerecrutement.model.Offre;
import com.iset.plateformerecrutement.requests.CandidatureDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/offre")
public class OffreController {

    private final OffreServiceImpl offreService;

    @Autowired
    public OffreController(OffreServiceImpl offreService) {
        this.offreService = offreService;
    }

    // ✅ Récupérer toutes les offres
    @GetMapping("/all")
    public List<Offre> getAllOffres() {
        return offreService.getAllOffre();
    }
    @GetMapping("/total")
    public long getTotalOffers() {
        return offreService.getTotalNumberOfOffers();
    }
    // ✅ Récupérer une offre par ID de l'offre
    @GetMapping("/{id}")
    public Optional<Offre> getOffreById(@PathVariable Long id) {
        return offreService.getOffreById(id);
    }

    @PostMapping("/creat")
    public ResponseEntity<Offre> createOffre(@RequestBody Offre offre) {
        Offre savedOffre = offreService.saveOffre(offre);
        return new ResponseEntity<>(savedOffre, HttpStatus.CREATED);
    }

    // ✅ Ajouter une nouvelle offre pour un recruteur
    @PostMapping("/save/{recruteurId}")
    public Offre saveOffre(@PathVariable Long recruteurId, @RequestBody Offre offre) {
        return offreService.addOffreByRecruteur(recruteurId, offre);
    }

    // ✅ Récupérer toutes les offres d'un recruteur par ID
    @GetMapping("/recruteur/{recruteurId}")
    public List<Offre> getOffresByRecruteur(@PathVariable Long recruteurId) {
        return offreService.getOffresByRecruteur(recruteurId);
    }

    // ✅ Mettre à jour une offre
    @PutMapping("/update/{id}")
    public Offre updateOffre(@PathVariable Long id, @RequestBody Offre offreDetails) {
        return offreService.updateOffre(id, offreDetails);
    }

    // ✅ Supprimer une offre par ID
    @DeleteMapping("/delete/{id}")
    public void deleteOffre(@PathVariable Long id) {
        offreService.deleteOffre(id);
    }


}
