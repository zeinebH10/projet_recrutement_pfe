package com.iset.plateformerecrutement.requests;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formation_Request {

    private String titre;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private String categorie;

    private Double prix;

    private LocalDate dateDebut;

    private LocalDate dateFin;
    private String prerequis;
    @Column(columnDefinition = "LONGTEXT")
    private String programme;
    private String nbre_participant;
    private String certification;
    private String duree;

}
