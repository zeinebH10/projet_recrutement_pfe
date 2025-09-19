package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model._Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository <_Token,Long> {
    @Query("select t from _Token t where t.user.email = ?1")
    _Token findByUserEmail(String email);
}
