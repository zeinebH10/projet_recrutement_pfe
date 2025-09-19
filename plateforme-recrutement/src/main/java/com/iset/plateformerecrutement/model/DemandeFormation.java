package com.iset.plateformerecrutement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class DemandeFormation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "LONGTEXT")
    private String motivation;
    private String teletravail;
    private String methode_apprentissage;
    private String langue;


    @ManyToOne
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;

    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;
}
