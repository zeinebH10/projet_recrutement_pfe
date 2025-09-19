package com.iset.plateformerecrutement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String categorie;
    private Double prix;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;
    private int nbVues = 0;
    private String prerequis;
    @Column(columnDefinition = "LONGTEXT")
    private String programme;
    private String nbre_participant;
    private String certification;
    private String duree;
    private String image;
    private double moyenneRating = 0.0;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL)
    private List<Rating> ratings;
    @ManyToOne
    @JoinColumn(name = "formateur_id")
    private Formateur formateur;


}

