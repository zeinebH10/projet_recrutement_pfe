/*package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByOffreId(Long offreId);
    List<Rating> findByFormationId(Long formationId);

    long countByOffreIdAndStarsLessThanEqual(Long offreId, int stars);

    long countByFormationIdAndStarsLessThanEqual(Long formationId, int stars);
    boolean existsByCandidatIdAndOffreId(Long candidatId, Long offreId);
    boolean existsByCandidatIdAndFormationId(Long candidatId, Long formationId);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.offre.id = :offreId")
    Optional<Double> calculateAverageRatingForOffre(@Param("offreId") Long offreId);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.formation.id = :formationId")
    Optional<Double> calculateAverageRatingForFormation(@Param("formationId") Long formationId);
}
*/
package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    boolean existsByCandidatIdAndOffreId(Long candidatId, Long offreId);
    boolean existsByCandidatIdAndFormationId(Long candidatId, Long formationId);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.offre.id = :offreId")
    Optional<Double> calculateAverageRatingForOffre(Long offreId);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.formation.id = :formationId")
    Optional<Double> calculateAverageRatingForFormation(Long formationId);

    long countByOffreIdAndStarsLessThanEqual(Long offreId, int stars);

    long countByFormationIdAndStarsLessThanEqual(Long formationId, int stars);

    List<Rating> findByOffreId(Long offreId);

    List<Rating> findByFormationId(Long formationId);
    @Query("SELECT r FROM Rating r JOIN FETCH r.candidat WHERE r.offre.id = :offreId")
    List<Rating> findByOffreWithCandidat(@Param("offreId") Long offreId);

}


