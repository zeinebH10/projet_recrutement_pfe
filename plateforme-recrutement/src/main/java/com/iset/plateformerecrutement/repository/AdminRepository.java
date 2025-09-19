package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model.Admin;
import com.iset.plateformerecrutement.model.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository  extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername (String username);
    Optional<Admin> findByEmail (String Email);

}
