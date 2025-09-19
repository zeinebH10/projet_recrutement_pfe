/*package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.RatingService;
import com.iset.plateformerecrutement.model.Rating;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        return ResponseEntity.ok(ratingService.getAllRating());
    }

    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody Rating rating) {
        try {
            Rating savedRating = ratingService.ajouterRating(rating);
            return ResponseEntity.ok(savedRating);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation échouée : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de l'ajout du rating.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRatingById(@PathVariable Long id) {
        return ratingService.getRatingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {
        try {
            ratingService.deleteRating(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erreur lors de la suppression du rating.");
        }
    }

    @GetMapping("/offre/{offreId}")
    public ResponseEntity<?> getRatingsByOffre(@PathVariable Long offreId) {
        return ResponseEntity.ok(ratingService.getRatingsByOffre(offreId));
    }

    @GetMapping("/formation/{formationId}")
    public ResponseEntity<?> getRatingsByFormation(@PathVariable Long formationId) {
        return ResponseEntity.ok(ratingService.getRatingsByFormation(formationId));
    }

    @GetMapping("/offre/{offreId}/average")
    public ResponseEntity<?> getAverageRatingForOffre(@PathVariable Long offreId) {
        Double average = ratingService.getAverageRatingForOffre(offreId);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/formation/{formationId}/average")
    public ResponseEntity<?> getAverageRatingForFormation(@PathVariable Long formationId) {
        Double average = ratingService.getAverageRatingForFormation(formationId);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/hasRated")
    public ResponseEntity<?> hasCandidateRated(
            @RequestParam Long candidatId,
            @RequestParam(required = false) Long offreId,
            @RequestParam(required = false) Long formationId) {

        if (offreId == null && formationId == null) {
            return ResponseEntity.badRequest().body("Vous devez fournir soit 'offreId' soit 'formationId'.");
        }

        if (offreId != null && formationId != null) {
            return ResponseEntity.badRequest().body("Vous ne pouvez fournir qu'un seul des deux paramètres : 'offreId' ou 'formationId'.");
        }

        boolean hasRated = ratingService.hasCandidateRated(candidatId, offreId, formationId);
        return ResponseEntity.ok(hasRated);
    }
}*/
package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.RatingService;
import com.iset.plateformerecrutement.model.Rating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@Slf4j
public class RatingController {

    private final RatingService ratingService;

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        try {
            List<Rating> ratings = ratingService.getAllRating();
            log.info("Récupération de {} ratings", ratings.size());
            return ResponseEntity.ok(ratings);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des ratings: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addRating(@RequestBody Rating rating) {
        try {
            Rating savedRating = ratingService.ajouterRating(rating);
            log.info("Rating ajouté avec succès, ID: {}", savedRating.getId());
            return ResponseEntity.ok(savedRating);
        } catch (IllegalArgumentException e) {
            log.warn("Validation échouée lors de l'ajout du rating: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Validation échouée : " + e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors de l'ajout du rating: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de l'ajout du rating.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRatingById(@PathVariable Long id) {
        try {
            return ratingService.getRatingById(id)
                    .map(rating -> {
                        log.info("Rating trouvé avec ID: {}", id);
                        return ResponseEntity.ok(rating);
                    })
                    .orElseGet(() -> {
                        log.warn("Rating non trouvé avec ID: {}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du rating ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de la récupération du rating.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Long id) {
        try {
            ratingService.deleteRating(id);
            log.info("Rating supprimé avec succès, ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.warn("Erreur de validation lors de la suppression du rating ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors de la suppression du rating ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de la suppression du rating.");
        }
    }

    @GetMapping("/offre/{offreId}")
    public ResponseEntity<?> getRatingsByOffre(@PathVariable Long offreId) {
        try {
            List<Rating> ratings = ratingService.getRatingsByOffre(offreId);
            log.info("Récupération de {} ratings pour l'offre ID: {}", ratings.size(), offreId);

            // Log détaillé pour debugging
            for (Rating rating : ratings) {
                log.debug("Rating ID: {}, Stars: {}, Candidat: {}, Commentaire: {}",
                        rating.getId(),
                        rating.getStars(),
                        rating.getCandidat() != null ? rating.getCandidat().getFullName() : "null",
                        rating.getCommentaire());
            }

            return ResponseEntity.ok(ratings);
        } catch (IllegalArgumentException e) {
            log.warn("Paramètre invalide pour getRatingsByOffre: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des ratings pour l'offre ID {}: {}", offreId, e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de la récupération des ratings.");
        }
    }

    @GetMapping("/formation/{formationId}")
    public ResponseEntity<?> getRatingsByFormation(@PathVariable Long formationId) {
        try {
            List<Rating> ratings = ratingService.getRatingsByFormation(formationId);
            log.info("Récupération de {} ratings pour la formation ID: {}", ratings.size(), formationId);
            return ResponseEntity.ok(ratings);
        } catch (IllegalArgumentException e) {
            log.warn("Paramètre invalide pour getRatingsByFormation: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des ratings pour la formation ID {}: {}", formationId, e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de la récupération des ratings.");
        }
    }

    @GetMapping("/offre/{offreId}/average")
    public ResponseEntity<?> getAverageRatingForOffre(@PathVariable Long offreId) {
        try {
            Double average = ratingService.getAverageRatingForOffre(offreId);
            log.info("Moyenne des ratings pour l'offre ID {}: {}", offreId, average);
            return ResponseEntity.ok(average);
        } catch (IllegalArgumentException e) {
            log.warn("Paramètre invalide pour getAverageRatingForOffre: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors du calcul de la moyenne pour l'offre ID {}: {}", offreId, e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors du calcul de la moyenne.");
        }
    }

    @GetMapping("/formation/{formationId}/average")
    public ResponseEntity<?> getAverageRatingForFormation(@PathVariable Long formationId) {
        try {
            Double average = ratingService.getAverageRatingForFormation(formationId);
            log.info("Moyenne des ratings pour la formation ID {}: {}", formationId, average);
            return ResponseEntity.ok(average);
        } catch (IllegalArgumentException e) {
            log.warn("Paramètre invalide pour getAverageRatingForFormation: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors du calcul de la moyenne pour la formation ID {}: {}", formationId, e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors du calcul de la moyenne.");
        }
    }

    @GetMapping("/hasRated")
    public ResponseEntity<?> hasCandidateRated(
            @RequestParam Long candidatId,
            @RequestParam(required = false) Long offreId,
            @RequestParam(required = false) Long formationId) {

        try {
            if (offreId == null && formationId == null) {
                return ResponseEntity.badRequest().body("Vous devez fournir soit 'offreId' soit 'formationId'.");
            }

            if (offreId != null && formationId != null) {
                return ResponseEntity.badRequest().body("Vous ne pouvez fournir qu'un seul des deux paramètres : 'offreId' ou 'formationId'.");
            }

            boolean hasRated = ratingService.hasCandidateRated(candidatId, offreId, formationId);
            log.info("Vérification rating candidat ID {} pour offre/formation: {}", candidatId, hasRated);
            return ResponseEntity.ok(hasRated);
        } catch (IllegalArgumentException e) {
            log.warn("Paramètres invalides pour hasCandidateRated: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("Erreur lors de la vérification du rating: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Erreur lors de la vérification.");
        }
    }
}
