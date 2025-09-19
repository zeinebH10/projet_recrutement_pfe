package com.iset.plateformerecrutement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class _Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_token;
    private String TokenText;
    private Date ExpiryDate;
    @OneToOne
    @JoinColumn(name = "id_user")
    private _User user;

}
