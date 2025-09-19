package com.iset.plateformerecrutement.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifiedTokenRequest {
    private String email;
    private String tokenText;
}
