package com.iset.plateformerecrutement.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatRequest {
    private String fullName;
    private String email;
    private LocalDate birthDate;
    private String username;
    private String password;
    private String niveauEtude;
    private String telephone;

}

