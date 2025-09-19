package com.iset.plateformerecrutement.controller;

import com.iset.plateformerecrutement.Impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    StorageService storageService;

    /**
     * Endpoint pour l'upload d'un fichier.
     */
    @PostMapping("/post")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            storageService.store(file, fileName);
            System.out.println("✅ Fichier uploadé avec succès : " + fileName);
            return ResponseEntity.ok("Fichier " + fileName + " uploadé avec succès !");
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'upload : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body("Échec de l'upload: " + e.getMessage());
        }
    }

    /**
     * Endpoint pour récupérer la liste des fichiers uploadés.
     */
    @GetMapping("/getallfiles")
    public ResponseEntity<List<String>> getListFiles(Model model) {
        List<String> fileNames = storageService.loadAll()
                .map(path -> MvcUriComponentsBuilder
                        .fromMethodName(UploadController.class, "getFile", path.getFileName().toString())
                        .build().toString())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(fileNames);
    }

    /**
     * Endpoint pour télécharger un fichier.
     */
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            // Charger le fichier
            Resource file = storageService.loadFile(filename);

            // Détecter le type MIME basé sur l'extension du fichier
            String contentType = "application/octet-stream";  // Valeur par défaut

            if (filename.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (filename.endsWith(".png")) {
                contentType = "image/png";
            } // Ajouter d'autres types de fichiers si nécessaire

            // Réponse avec le fichier et l'en-tête approprié
            System.out.println("📥 Téléchargement du fichier : " + filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, contentType)  // Le type MIME est maintenant défini dynamiquement
                    .body(file);
        } catch (Exception e) {
            // En cas d'erreur, afficher l'erreur dans la console et renvoyer une erreur 404
            System.err.println("❌ Fichier introuvable : " + filename);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
