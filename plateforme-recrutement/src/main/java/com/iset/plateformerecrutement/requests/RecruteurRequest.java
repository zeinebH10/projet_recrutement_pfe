package com.iset.plateformerecrutement.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecruteurRequest {
    private String fullName;
    private LocalDate birthDate;
    private String nomEntreprise;
    private String description;
    private String specialite;
    private String siteWeb;
    private String telephone;
    private String compteLinkedin;
    private String adresse;
}
