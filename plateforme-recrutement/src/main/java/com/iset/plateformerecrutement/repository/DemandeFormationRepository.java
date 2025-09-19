package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.DemandeFormation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DemandeFormationRepository extends JpaRepository<DemandeFormation, Long> {

    @Query("SELECT d FROM DemandeFormation d WHERE d.candidat.id = :candidatId")
    List<DemandeFormation> findByCandidatId(@Param("candidatId") Long candidatId);

    @Query("SELECT d FROM DemandeFormation d WHERE d.formation.id = :formationId")
    List<DemandeFormation> findByFormationId(@Param("formationId") Long formationId);

    @Query("SELECT d FROM DemandeFormation d JOIN d.formation o WHERE o.formateur.id = :formateurId")
    List<DemandeFormation> findByFormateurId(@Param("formateurId") Long formateurId);

}
