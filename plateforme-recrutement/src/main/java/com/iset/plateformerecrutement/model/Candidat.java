package com.iset.plateformerecrutement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
public class Candidat extends _User {

    @ElementCollection
    private List<String> competences;

    private String niveauEtude;

    @Lob
    private String image;

    private String adresse;
    private String telephone;
    private String compteLinkedin;
    @Column(columnDefinition = "LONGTEXT")
    private String experience;
    private String langue;

    private int badRatingCount = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DemandeStage> demandesStage;

    @JsonIgnore
    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DemandeFormation> demandeformation;




    public Candidat(String fullName, String email, LocalDate birthDate, String username, String password,
                    boolean isEnabled, _Role role, LocalDateTime registrationDate, List<String> competences, String niveauEtude, String image,
                    String adresse, String telephone, String compteLinkedin, String experience, String langue) {
        super(0, fullName, email, birthDate, username, password, isEnabled, role, registrationDate, null);

        this.competences = competences;
        this.niveauEtude = niveauEtude;
        this.image = image;
        this.adresse = adresse;
        this.telephone = telephone;
        this.compteLinkedin = compteLinkedin;
        this.experience=experience;
        this.langue=langue;
    }
}