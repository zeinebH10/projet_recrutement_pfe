package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.*;
import com.iset.plateformerecrutement.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RatingService {

    private static final int BAD_RATING_THRESHOLD = 2;
    private static final int NOTIFICATION_THRESHOLD = 5;

    private final RatingRepository ratingRepository;
    private final OffreRepository offreRepository;
    private final FormationRepository formationRepository;
    private final CandidatRepository candidatRepository;
    private final NotificationRepository notificationRepository;






    private void handleBadRatingsForOffre(Rating rating) {
        if (rating.getOffre() != null) {
            Long offreId = rating.getOffre().getId();
            handleBadRatingsForEntity("offre", offreId,
                    "L'offre (ID: " + offreId + ") a reçu 5 mauvaises évaluations.",
                    "BAD_OFFER_RATING");
        }
    }

    private void handleBadRatingsForFormation(Rating rating) {
        if (rating.getFormation() != null) {
            Long formationId = rating.getFormation().getId();
            handleBadRatingsForEntity("formation", formationId,
                    "La formation (ID: " + formationId + ") a reçu 5 mauvaises évaluations.",
                    "BAD_FORMATION_RATING");
        }
    }

    private void updateAverageRatings(Rating rating) {
        try {
            // Recalculer la moyenne pour l'offre
            if (rating.getOffre() != null) {
                recalculerMoyenneOffre(rating.getOffre().getId());
            }

            // Recalculer la moyenne pour la formation
            if (rating.getFormation() != null) {
                recalculerMoyenneFormation(rating.getFormation().getId());
            }
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour des moyennes: {}", e.getMessage());
        }
    }




    @Transactional(readOnly = true)
    public List<Rating> getAllRating() {
        try {
            return ratingRepository.findAll();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de tous les ratings: {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des ratings", e);
        }
    }

    public Rating ajouterRating(Rating rating) {
        validateRating(rating);
        checkForDuplicateRating(rating);

        Candidat candidat = candidatRepository.findById(rating.getCandidat().getId())
                .orElseThrow(() -> new IllegalArgumentException("Le candidat spécifié n'existe pas"));
        rating.setCandidat(candidat);

        if (rating.getOffre() != null) {
            Offre offre = offreRepository.findById(rating.getOffre().getId())
                    .orElseThrow(() -> new IllegalArgumentException("L'offre spécifiée n'existe pas"));
            rating.setOffre(offre);
        }

        if (rating.getFormation() != null) {
            Formation formation = formationRepository.findById(rating.getFormation().getId())
                    .orElseThrow(() -> new IllegalArgumentException("La formation spécifiée n'existe pas"));
            rating.setFormation(formation);
        }

        Rating savedRating = ratingRepository.save(rating);
        log.info("Rating sauvegardé avec succès, ID: {}", savedRating.getId());

        processBadRatings(savedRating);
        updateAverageRatings(savedRating);

        return savedRating;
    }

    private void validateRating(Rating rating) {
        if (rating == null) {
            throw new IllegalArgumentException("Le rating ne peut pas être null");
        }
        if (rating.getStars() < 1 || rating.getStars() > 5) {
            throw new IllegalArgumentException("Le nombre d'étoiles doit être entre 1 et 5");
        }
        if (rating.getCandidat() == null) {
            throw new IllegalArgumentException("Un candidat doit être associé au rating");
        }
        if (rating.getOffre() == null && rating.getFormation() == null) {
            throw new IllegalArgumentException("Une offre ou une formation doit être associée au rating");
        }
    }

    private void checkForDuplicateRating(Rating rating) {
        boolean alreadyRated;
        if (rating.getOffre() != null) {
            alreadyRated = ratingRepository.existsByCandidatIdAndOffreId(
                    rating.getCandidat().getId(),
                    rating.getOffre().getId());
        } else {
            alreadyRated = ratingRepository.existsByCandidatIdAndFormationId(
                    rating.getCandidat().getId(),
                    rating.getFormation().getId());
        }
        if (alreadyRated) {
            throw new IllegalArgumentException("Le candidat a déjà noté cette offre/formation");
        }
    }

    @Transactional(readOnly = true)
    public boolean hasCandidateRated(Long candidatId, Long offreId, Long formationId) {
        if (candidatId == null) {
            throw new IllegalArgumentException("L'ID du candidat ne peut pas être null");
        }
        if (offreId != null) {
            return ratingRepository.existsByCandidatIdAndOffreId(candidatId, offreId);
        } else if (formationId != null) {
            return ratingRepository.existsByCandidatIdAndFormationId(candidatId, formationId);
        } else {
            throw new IllegalArgumentException("Il faut fournir soit un ID d'offre, soit un ID de formation");
        }
    }

    private void processBadRatings(Rating rating) {
        if (rating.getStars() <= BAD_RATING_THRESHOLD) {
            handleBadRatingForCandidat(rating);
            if (rating.getOffre() != null) {
                handleBadRatingsForEntity("offre", rating.getOffre().getId(),
                        "L'offre (ID: " + rating.getOffre().getId() + ") a reçu 5 mauvaises évaluations.",
                        "BAD_OFFER_RATING");
            }
            if (rating.getFormation() != null) {
                handleBadRatingsForEntity("formation", rating.getFormation().getId(),
                        "La formation (ID: " + rating.getFormation().getId() + ") a reçu 5 mauvaises évaluations.",
                        "BAD_FORMATION_RATING");
            }
        }
    }

    private void handleBadRatingForCandidat(Rating rating) {
        Candidat candidat = rating.getCandidat();
        if (candidat != null) {
            candidat.setBadRatingCount(candidat.getBadRatingCount() + 1);
            if (candidat.getBadRatingCount() >= NOTIFICATION_THRESHOLD) {
                createAndSaveNotification(
                        "Le candidat " + candidat.getFullName() + " a soumis 5 mauvaises évaluations.",
                        candidat,
                        "BAD_RATING",
                        true);
            }
            candidatRepository.save(candidat);
            log.info("BadRatingCount mis à jour pour le candidat ID: {}, nouveau count: {}",
                    candidat.getId(), candidat.getBadRatingCount());
        }
    }

    private void handleBadRatingsForEntity(String entityType, Long entityId, String message, String type) {
        long badRatingsCount = 0;
        if ("offre".equals(entityType)) {
            badRatingsCount = ratingRepository.countByOffreIdAndStarsLessThanEqual(entityId, BAD_RATING_THRESHOLD);
        } else if ("formation".equals(entityType)) {
            badRatingsCount = ratingRepository.countByFormationIdAndStarsLessThanEqual(entityId, BAD_RATING_THRESHOLD);
        }

        if (badRatingsCount >= NOTIFICATION_THRESHOLD) {
            createAndSaveNotification(message, null, type, true);
            log.info("Notification créée pour {} mauvais ratings sur {} ID: {}", badRatingsCount, entityType, entityId);
        }
    }

    private void createAndSaveNotification(String message, Candidat candidat, String type, boolean forAdmin) {
        Notification notif = new Notification();
        notif.setMessage(message);
        notif.setType(type);
        notif.setForAdmin(forAdmin);
        if (candidat != null) {
            notif.setCandidat(candidat);
        }
        notificationRepository.save(notif);
        log.info("Notification créée avec succès: {}", message);
    }

    @Transactional(readOnly = true)
    public double getAverageRatingForOffre(Long offreId) {
        if (offreId == null) {
            throw new IllegalArgumentException("L'ID de l'offre ne peut pas être null");
        }
        return ratingRepository.calculateAverageRatingForOffre(offreId).orElse(0.0);
    }

    @Transactional(readOnly = true)
    public double getAverageRatingForFormation(Long formationId) {
        if (formationId == null) {
            throw new IllegalArgumentException("L'ID de la formation ne peut pas être null");
        }
        return ratingRepository.calculateAverageRatingForFormation(formationId).orElse(0.0);
    }

    public void recalculerMoyenneOffre(Long offreId) {
        double moyenne = getAverageRatingForOffre(offreId);
        offreRepository.findById(offreId).ifPresent(offre -> {
            offre.setMoyenneRating(moyenne);
            offreRepository.save(offre);
            log.info("Moyenne recalculée pour l'offre ID {}: {}", offreId, moyenne);
        });
    }

    public void recalculerMoyenneFormation(Long formationId) {
        double moyenne = getAverageRatingForFormation(formationId);
        formationRepository.findById(formationId).ifPresent(formation -> {
            formation.setMoyenneRating(moyenne);
            formationRepository.save(formation);
            log.info("Moyenne recalculée pour la formation ID {}: {}", formationId, moyenne);
        });
    }

    @Transactional(readOnly = true)
    public List<Rating> getRatingsByOffre(Long offreId) {
        if (offreId == null) {
            throw new IllegalArgumentException("L'ID de l'offre ne peut pas être null");
        }
        return ratingRepository.findByOffreId(offreId);
    }

    @Transactional(readOnly = true)
    public List<Rating> getRatingsByFormation(Long formationId) {
        if (formationId == null) {
            throw new IllegalArgumentException("L'ID de la formation ne peut pas être null");
        }
        return ratingRepository.findByFormationId(formationId);
    }

    @Transactional(readOnly = true)
    public Optional<Rating> getRatingById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
        return ratingRepository.findById(id);
    }

    public void deleteRating(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Optional<Rating> ratingOpt = ratingRepository.findById(id);
        if (ratingOpt.isPresent()) {
            Rating rating = ratingOpt.get();
            ratingRepository.deleteById(id);

            if (rating.getOffre() != null) {
                recalculerMoyenneOffre(rating.getOffre().getId());
            }
            if (rating.getFormation() != null) {
                recalculerMoyenneFormation(rating.getFormation().getId());
            }

            log.info("Rating ID {} supprimé avec succès", id);
        } else {
            throw new IllegalArgumentException("Rating avec ID " + id + " non trouvé");
        }
    }
}
