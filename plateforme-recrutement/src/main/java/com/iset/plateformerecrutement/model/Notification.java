package com.iset.plateformerecrutement.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateEnvoi;

    private boolean lu;
    private String type; // Exemple: "ACCEPTEE", "REFUSEE", "RENDEZ_VOUS", etc.
    private boolean isForAdmin = false;
    @ManyToOne
    @JoinColumn(name = "candidat_id")
    private Candidat candidat;

    @ManyToOne
    @JoinColumn(name = "recruteur_id")
    private Recruteur recruteur;

    @ManyToOne
    @JoinColumn(name = "Formateur_id")
    private Formateur formateur;

    @PrePersist
    public void prePersist() {
        this.dateEnvoi = LocalDateTime.now();
    }
}

