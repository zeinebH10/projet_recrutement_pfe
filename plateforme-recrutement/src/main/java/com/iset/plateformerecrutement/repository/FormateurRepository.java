package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Formateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FormateurRepository extends JpaRepository<Formateur, Long> {
    Optional<Formateur> findByUsername (String username);
    Optional<Formateur> findByEmail (String Email);
}
