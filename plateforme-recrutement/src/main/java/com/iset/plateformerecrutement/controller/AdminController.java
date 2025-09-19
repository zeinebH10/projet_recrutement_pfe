package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.StatisticsServiceImpl;
import com.iset.plateformerecrutement.model.*;
import com.iset.plateformerecrutement.Impl.AdminServiceImpl;
import com.iset.plateformerecrutement.requests.AdminRequest;
import com.iset.plateformerecrutement.requests.StatisticsDTO;
import com.iset.plateformerecrutement.requests.SuccessMessageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/admin")
public class AdminController {

    private final AdminServiceImpl adminService;
    private final StatisticsServiceImpl statisticsService;

    @Autowired
    public AdminController(AdminServiceImpl adminService, StatisticsServiceImpl statisticsService) {
        this.adminService = adminService;

        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDTO> getStatistics() {
        StatisticsDTO stats = statisticsService.getStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}")
    public Optional<Admin> getAdminById(@PathVariable Long id) {
        return adminService.getAdminParId(id);
    }

@PostMapping("/auth/register")
    public ResponseEntity<?> registerAdmin(@RequestBody AdminRequest adminRequest) {
        try {
            if (adminRequest.getUsername() == null || adminRequest.getUsername().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le champ username ne peut pas être vide !");
            }

            adminService.registerAdmin(adminRequest);
            return ResponseEntity.ok(new SuccessMessageRequest("admin enregistré avec succès !"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
