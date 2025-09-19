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
public class CompetenceOffre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomCompetence;
    private boolean obligatoire;

    @ManyToOne
    @JoinColumn(name = "offre_id", nullable = false)
    private Offre offre;
}
