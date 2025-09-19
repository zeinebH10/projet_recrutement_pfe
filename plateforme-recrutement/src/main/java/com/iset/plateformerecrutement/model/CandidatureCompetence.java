package com.iset.plateformerecrutement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class CandidatureCompetence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "demande_stage_id", nullable = false)
    private DemandeStage demandeStage;

    @ManyToOne
    @JoinColumn(name = "competence_offre_id", nullable = false)
    private CompetenceOffre competenceOffre;

    private boolean selectionne; // Si le candidat a sélectionné cette compétence
}
