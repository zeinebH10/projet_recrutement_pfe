package com.iset.plateformerecrutement.requests;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formateur_Request {
    private String fullName;
    private LocalDate birthDate;
    private String specialite;
    @Column(columnDefinition = "LONGTEXT")
    private String experience;
    private String diplome;
    private String telephone;
    private String compteLinkedin;
    private String adresse;
    private String nomCentre;
    private String registreCommerce;
    private String siteWebCentre;
    //private String image;
}
