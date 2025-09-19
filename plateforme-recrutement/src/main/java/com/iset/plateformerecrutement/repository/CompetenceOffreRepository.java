package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.CompetenceOffre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetenceOffreRepository extends JpaRepository<CompetenceOffre, Long> {
    List<CompetenceOffre> findByOffreId(Long offreId);
}