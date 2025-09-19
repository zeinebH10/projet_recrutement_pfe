package com.iset.plateformerecrutement.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class OffreDTO {
    private Long id;
    private String titre;
    private String description;
    private String competences;
    private String domaine;
    private String localisation;
    private String type;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
