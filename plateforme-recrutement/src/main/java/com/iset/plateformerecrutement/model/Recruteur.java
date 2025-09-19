package com.iset.plateformerecrutement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
public class Recruteur extends _User {

    private String nomEntreprise;
    @Column(columnDefinition = "LONGTEXT")
    private String description;
    private String specialite;
    private String image;
    private String siteWeb;
    private String telephone;
    private String compteLinkedin;
    private String adresse;

    @JsonIgnore
    @OneToMany(mappedBy = "recruteur", fetch = FetchType.LAZY)
    private List<Offre> offresPubliees;

}
