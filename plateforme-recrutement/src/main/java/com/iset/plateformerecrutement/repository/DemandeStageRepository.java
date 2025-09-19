package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.DemandeStage;
import com.iset.plateformerecrutement.model.StatutCandidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DemandeStageRepository extends JpaRepository<DemandeStage, Long> {
    @Query("SELECT d FROM DemandeStage d WHERE d.candidat.id = :candidatId")
    List<DemandeStage> findByCandidatId(@Param("candidatId") Long candidatId);
    @Query("SELECT d FROM DemandeStage d WHERE d.offre.id = :offreId")
    List<DemandeStage> findByOffreStageId(@Param("offreId") Long offreId);

    @Query("SELECT d FROM DemandeStage d JOIN d.offre o WHERE o.recruteur.id = :recruteurId")
    List<DemandeStage> findByRecruteurId(@Param("recruteurId") Long recruteurId);

    Optional<DemandeStage> findByCandidatIdAndOffreId(Long candidatId, Long offreId);

    @Query("SELECT COUNT(d) FROM DemandeStage d WHERE d.statut = :statut")
    Long countByStatut(@Param("statut") StatutCandidature statut);
}



