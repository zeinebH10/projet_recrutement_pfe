package com.iset.plateformerecrutement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String type;
    private String domaine;
    private String teletravail;
    private String localisation;
    @Column(columnDefinition = "LONGTEXT")
    private String technologies;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    private int nombreStagiaire;
    @Column(columnDefinition = "LONGTEXT")
    private String competences;//exigence de lemploi
    private String experience;
    private String niveauEtude;
    private String langue;
    private boolean payment;
    private double moyenneRating = 0.0;

    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL)
    private List<Rating> ratings;
    @ManyToOne
    @JoinColumn(name = "recruteur_id")
    private Recruteur recruteur;
    @JsonIgnore
    @OneToMany(mappedBy = "offre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DemandeStage> demandes;

    // Calculer la durée du stage
    public long getDureeStage() {
        if (dateDebut != null && dateFin != null) {
            return java.time.Duration.between(dateDebut.atStartOfDay(), dateFin.atStartOfDay()).toDays();
        }
        return 0;
    }

    public Boolean isPayment() {
        return payment;
    }

}
