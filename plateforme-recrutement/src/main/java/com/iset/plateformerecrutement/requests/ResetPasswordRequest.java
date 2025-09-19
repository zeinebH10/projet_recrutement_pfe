package com.iset.plateformerecrutement.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    private String email;
    private String tokenText;
    private String newPassword;
    private String confirmPassword;
}

