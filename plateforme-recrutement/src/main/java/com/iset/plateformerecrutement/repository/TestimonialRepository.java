package com.iset.plateformerecrutement.repository;


import com.iset.plateformerecrutement.model.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, Long> {
}
