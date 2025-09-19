package com.iset.plateformerecrutement.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Formateur_Register_Request {
    private String fullName;
    private String email;
    private LocalDate birthDate;
    private String username;
    private String password;
    private String registreCommerce;
    private String telephone;
}
