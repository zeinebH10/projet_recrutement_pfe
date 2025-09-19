package com.iset.plateformerecrutement.Impl;


import com.iset.plateformerecrutement.model.*;
import com.iset.plateformerecrutement.repository.*;
import com.iset.plateformerecrutement.requests.AdminRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;

    }


    public Optional<Admin> getAdminParId(Long id) {
        return adminRepository.findById(id);
    }



    public Admin registerAdmin(AdminRequest adminRequest) {
        Optional<Admin> existingUserByEmail = adminRepository.findByEmail(adminRequest.getEmail());
        Optional<Admin> existingUserByUsername = adminRepository.findByUsername(adminRequest.getUsername());

        if (existingUserByEmail.isPresent() || existingUserByUsername.isPresent()) {
            throw new RuntimeException("Un utilisateur avec cet email ou nom d'utilisateur existe déjà !");
        }

        Admin admin = Admin.builder()
                .fullName(adminRequest.getFullName())
                .email(adminRequest.getEmail())
                .birthDate(adminRequest.getBirthDate())
                .username(adminRequest.getUsername())
                .password(passwordEncoder.encode(adminRequest.getPassword()))
                .isEnabled(true)
                .role(_Role.ROLE_ADMIN)
                .build();


        Admin savedadmin = adminRepository.save(admin);
        System.out.println("admin après enregistrement : " + savedadmin);

        return savedadmin;
    }


}
