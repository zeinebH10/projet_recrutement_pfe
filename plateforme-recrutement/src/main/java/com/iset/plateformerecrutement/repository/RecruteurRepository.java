package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Recruteur;
import com.iset.plateformerecrutement.model._User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruteurRepository  extends JpaRepository<Recruteur, Long> {

    Optional<Recruteur> findByUsername (String username);
    Optional<Recruteur> findByEmail (String Email);

    long countByIsEnabledTrue();
}
