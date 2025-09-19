package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.Testimonial;
import com.iset.plateformerecrutement.model._User;
import com.iset.plateformerecrutement.repository.TestimonialRepository;
import com.iset.plateformerecrutement.repository.UserRepository;

import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TestimonialService {


    @Autowired
    private TestimonialRepository testimonialRepository;
    @Autowired
    private UserRepository userRepository ;

    public List<Testimonial> getAllTestimonials() {
        return testimonialRepository.findAll();
    }


    public void addTestimonial(Long id ,Testimonial testimonialDetail) {
        _User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User  non trouvé avec l'ID: " + id));
        Testimonial testimonial = new Testimonial();
        testimonial.setMessage(testimonialDetail.getMessage());
        testimonial.setRating(testimonialDetail.getRating());
        testimonial.setUser(user);
        testimonialRepository.save(testimonial);
    }
}
