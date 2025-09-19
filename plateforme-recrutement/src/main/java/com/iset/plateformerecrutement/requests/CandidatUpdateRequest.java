package com.iset.plateformerecrutement.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatUpdateRequest {
    private String fullName;
    private LocalDate birthDate;
    private String niveauEtude;
    private List<String> competences;
    private String adresse;
    private String telephone;
    private String compteLinkedin;
    private String experience;
    private String langue;
}
