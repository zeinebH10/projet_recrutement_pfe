package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.CandidatureCompetence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureCompetenceRepository extends JpaRepository<CandidatureCompetence, Long> {
    List<CandidatureCompetence> findByDemandeStageId(Long demandeStageId);
    List<CandidatureCompetence> findByDemandeStageIdAndSelectionne(Long demandeStageId, boolean selectionne);
}