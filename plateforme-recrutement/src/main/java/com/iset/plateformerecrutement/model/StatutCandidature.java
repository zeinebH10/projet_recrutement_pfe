package com.iset.plateformerecrutement.model;

public enum StatutCandidature {
    EN_ATTENTE("En attente"),
    ACCEPTEE("Acceptée"),
    REFUSEE("Refusée");

    private final String libelle;

    StatutCandidature(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

}
