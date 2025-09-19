package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidatRepository  extends JpaRepository<Candidat, Long> {
    Optional<Candidat> findByUsername (String username);
    Optional<Candidat> findByEmail (String Email);


}
