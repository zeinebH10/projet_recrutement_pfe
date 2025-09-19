package com.iset.plateformerecrutement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
public class Formateur extends _User {

    private String specialite;
    @Column(columnDefinition = "LONGTEXT")
    private String experience;
    private String diplome;
    private String telephone;
    private String compteLinkedin;
    private String adresse;
    private String image;
    private String nomCentre;
    private String registreCommerce;
    private String siteWebCentre;

    @JsonIgnore
    @OneToMany(mappedBy = "formateur", fetch = FetchType.LAZY)
    private List<Formation> formations;
}
