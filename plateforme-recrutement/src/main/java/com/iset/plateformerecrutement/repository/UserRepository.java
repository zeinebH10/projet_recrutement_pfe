package com.iset.plateformerecrutement.repository;

import com.iset.plateformerecrutement.model._Role;
import com.iset.plateformerecrutement.model._User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<_User,Long> {
Optional<_User> findByUsername (String username);

Optional<_User> findByEmail (String Email);


    @Query("SELECT DATE(u.registrationDate) as date, COUNT(u) as count " +
            "FROM _User u " +
            "WHERE DATE(u.registrationDate) BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(u.registrationDate) " +
            "ORDER BY DATE(u.registrationDate)")
    List<Object[]> countRegistrationsByDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    long countByRole(_Role role);

}
