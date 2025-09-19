package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FormationRepository extends JpaRepository<Formation, Long> {
    @Query("SELECT f FROM Formation f WHERE f.formateur.id = :formateurId")
    List<Formation> findFormationsByFormateurId(@Param("formateurId") Long formateurId);
}
