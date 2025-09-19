package com.iset.plateformerecrutement.controller;


import com.iset.plateformerecrutement.Impl.TestimonialService;

import com.iset.plateformerecrutement.model.Testimonial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testimonials")
public class TestimonialController {

    @Autowired
    private TestimonialService testimonialService;

    @GetMapping
    public List<Testimonial> getTestimonials() {
        return testimonialService.getAllTestimonials();
    }

    @PostMapping("/{id}")
    public void addTestimonial(@PathVariable Long id, @RequestBody Testimonial testimonialdetail) {
        testimonialService.addTestimonial(id, testimonialdetail);
    }
}
