package com.iset.plateformerecrutement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class DemandeStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String cv;
    private String lettre_motivation;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateDemande = new Date();
    // Relation Many-to-One avec Candidat
    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;

    @ManyToOne
    @JoinColumn(name = "offre_id", nullable = false)
    private Offre offre;

    @Enumerated(EnumType.STRING)
    private StatutCandidature statut = StatutCandidature.EN_ATTENTE;
}
