package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OffreRepository extends JpaRepository<Offre, Long> {

    @Query("SELECT o FROM Offre o WHERE o.recruteur.id = :recruteurId")
    List<Offre> findByRecruteurId(@Param("recruteurId") Long recruteurId);
    long count();
}
