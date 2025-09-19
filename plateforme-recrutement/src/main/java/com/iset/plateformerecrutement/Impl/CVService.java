package com.iset.plateformerecrutement.Impl;

import com.iset.plateformerecrutement.model.Offre;
import com.iset.plateformerecrutement.repository.OffreRepository;
import com.iset.plateformerecrutement.requests.OffreDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CVService {

    private final OffreRepository offreRepository;

    // Liste étendue des compétences techniques et non-techniques
    private static final List<String> ALL_KNOWN_SKILLS = List.of(
            // Techniques
            "java", "spring", "spring boot", "angular", "php", "html", "css",
            "mysql", "oracle", "flutter", "android studio", "linux", "symfony",
            "bootstrap", "python", "docker", "git", "javascript", "typescript",

            // Non-techniques
            "communication", "réseaux sociaux", "rédaction", "travail d'équipe",
            "gestion de projet", "marketing", "analyse", "leadership", "excel",
            "word", "aérobie", "lecture", "langues", "français", "anglais", "arabe"
    );

    // Dictionnaire de synonymes
    private static final Map<String, List<String>> SYNONYMS = Map.of(
            "communication", List.of("relationnel", "écrite", "orale"),
            "java", List.of("j2ee", "jdk"),
            "réseaux sociaux", List.of("facebook", "instagram", "linkedin", "twitter"),
            "spring boot", List.of("springboot"),
            "travail d'équipe", List.of("collaboration", "équipe")
    );

    public String extractTextFromPDF(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    public List<String> detectSkills(String extractedText) {
        String textNormalise = extractedText.toLowerCase()
                .replaceAll("[^a-zéèàêâùûîôäëüïöÿç ]", " ");

        // Détection des compétences directes
        Set<String> detected = ALL_KNOWN_SKILLS.stream()
                .filter(skill -> {
                    String regex = "\\b" + Pattern.quote(skill.toLowerCase()) + "\\b";
                    return Pattern.compile(regex).matcher(textNormalise).find();
                })
                .collect(Collectors.toSet());

        // Détection des synonymes
        SYNONYMS.forEach((mainSkill, synonyms) -> {
            synonyms.forEach(synonym -> {
                if (textNormalise.contains(synonym)) {
                    detected.add(mainSkill);
                }
            });
        });

        return new ArrayList<>(detected);
    }

    public List<OffreDTO> findMatchingOffers(List<String> detectedSkills) {
        if (detectedSkills.isEmpty()) {
            return Collections.emptyList();
        }

        List<Offre> allOffres = offreRepository.findAll();

        return allOffres.stream()
                .map(offre -> {
                    // Combiner competences + experience pour analyse
                    String texteOffre = (offre.getCompetences() != null ? offre.getCompetences() + " " : "")
                            + (offre.getExperience() != null ? offre.getExperience() : "")
                            + (offre.getDescription() != null ? " " + offre.getDescription() : "");

                    // Extraire les mots-clés de l'offre
                    List<String> offreKeywords = Arrays.stream(texteOffre.toLowerCase().split("[ ,;./()\n]+"))
                            .map(String::trim)
                            .filter(word -> !word.isEmpty() && word.length() > 2)
                            .collect(Collectors.toList());

                    // Calcul du score de matching
                    long matchingScore = detectedSkills.stream()
                            .filter(skill -> offreKeywords.contains(skill.toLowerCase()))
                            .count();

                    // Bonus pour les compétences principales
                    if (offre.getDomaine() != null && offre.getDomaine().equalsIgnoreCase("Informatique")) {
                        if (detectedSkills.contains("java")) matchingScore += 2;
                        if (detectedSkills.contains("angular")) matchingScore += 2;
                    }

                    return Map.entry(offre, matchingScore);
                })
                .filter(entry -> entry.getValue() > 0) // Au moins une compétence correspondante
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // Tri par score décroissant
                .map(entry -> mapToDTO(entry.getKey()))
                .collect(Collectors.toList());
    }

    private OffreDTO mapToDTO(Offre offre) {
        return new OffreDTO(
                offre.getId(),
                offre.getTitre(),
                offre.getDescription(),
                offre.getCompetences(),
                offre.getDomaine(),
                offre.getLocalisation(),
                offre.getType(),
                offre.getDateDebut(),
                offre.getDateFin()
        );
    }
}