package com.iset.plateformerecrutement.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemandeFormationRequest {
    private String motivation;
    private String teletravail;
    private String methode_apprentissage;
    private String langue;
}
