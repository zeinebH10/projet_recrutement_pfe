package com.iset.plateformerecrutement.requests;

import java.util.List;

public class CandidatureDTO {
    private Long candidatId;
    private Long offreId;
    private List<Long> competencesSelectionnees; // IDs des CompetenceOffre sélectionnées

    // Constructeurs
    public CandidatureDTO() {}

    public CandidatureDTO(Long candidatId, Long offreId, List<Long> competencesSelectionnees) {
        this.candidatId = candidatId;
        this.offreId = offreId;
        this.competencesSelectionnees = competencesSelectionnees;
    }

    // Getters et Setters
    public Long getCandidatId() {
        return candidatId;
    }

    public void setCandidatId(Long candidatId) {
        this.candidatId = candidatId;
    }

    public Long getOffreId() {
        return offreId;
    }

    public void setOffreId(Long offreId) {
        this.offreId = offreId;
    }

    public List<Long> getCompetencesSelectionnees() {
        return competencesSelectionnees;
    }

    public void setCompetencesSelectionnees(List<Long> competencesSelectionnees) {
        this.competencesSelectionnees = competencesSelectionnees;
    }
}
