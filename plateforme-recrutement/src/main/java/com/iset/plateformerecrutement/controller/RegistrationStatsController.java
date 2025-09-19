package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.model.StatutCandidature;
import com.iset.plateformerecrutement.repository.DemandeStageRepository;
import com.iset.plateformerecrutement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/stats")
@CrossOrigin(origins = "*")
public class RegistrationStatsController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DemandeStageRepository candidatureRepository;

    @GetMapping("/registrations")
    public ResponseEntity<Map<String, Object>> getRegistrationStats(
            @RequestParam(defaultValue = "7") int days) {

        // Validation
        if (days != 7 && days != 30 && days != 90) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Seules les périodes de 7, 30 ou 90 jours sont acceptées"));
        }

        // Calcul des dates
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        // Récupération des données
        List<Object[]> rawStats = userRepository.countRegistrationsByDate(startDate, endDate);

        // Formatage des données pour Chart.js
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        // Remplir les dates manquantes avec 0
        Map<LocalDate, Long> statsMap = new LinkedHashMap<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            statsMap.put(currentDate, 0L);
            currentDate = currentDate.plusDays(1);
        }

        // Mettre à jour avec les données réelles
        for (Object[] stat : rawStats) {
            LocalDate date = ((java.sql.Date) stat[0]).toLocalDate();
            Long count = (Long) stat[1];
            statsMap.put(date, count);
        }

        // Préparation des données finales
        for (Map.Entry<LocalDate, Long> entry : statsMap.entrySet()) {
            labels.add(entry.getKey().format(DateTimeFormatter.ofPattern("dd MMM")));
            data.add(entry.getValue());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);
        response.put("total", data.stream().mapToLong(Long::longValue).sum());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/demande-status")
    public Map<String, Long> getDemandeStageStats() {
        // 👉 C’est ICI qu’on met tes lignes
        Long nbAcceptees = candidatureRepository.countByStatut(StatutCandidature.ACCEPTEE);
        Long nbEnAttente = candidatureRepository.countByStatut(StatutCandidature.EN_ATTENTE);
        Long nbRefusees = candidatureRepository.countByStatut(StatutCandidature.REFUSEE);

        Map<String, Long> stats = new HashMap<>();
        stats.put("ACCEPTEE", nbAcceptees);
        stats.put("EN_ATTENTE", nbEnAttente);
        stats.put("REFUSEE", nbRefusees);

        return stats;  // 👉 ça sera renvoyé en JSON à Angular
    }
}
