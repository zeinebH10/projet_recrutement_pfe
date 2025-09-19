package com.iset.plateformerecrutement.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.stream.Stream;

@Service
public class StorageService {

    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("upload-dir");

    /**
     * Constructeur : Initialise le dossier de stockage au démarrage.
     */
    public StorageService() {
        init();
    }

    /**
     * Stocke un fichier dans le dossier upload-dir.
     */
    public void store(MultipartFile file, String fileName) {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            Path destinationFile = this.rootLocation.resolve(fileName);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            log.info("✅ Fichier stocké avec succès : " + destinationFile.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("❌ Erreur lors du stockage du fichier : " + e.getMessage());
        }
    }

    /**
     * Charge un fichier à partir du stockage.
     */
    public Resource loadFile(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("❌ Impossible de lire le fichier !");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("❌ URL mal formée !");
        }
    }

    /**
     * Charge tous les fichiers du dossier.
     */
    public Stream<Path> loadAll() {
        try {
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(rootLocation))
                    .map(rootLocation::relativize);
        } catch (IOException e) {
            throw new RuntimeException("❌ Impossible de charger les fichiers !");
        }
    }

    /**
     * Supprime tous les fichiers du stockage.
     */
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Initialise le dossier de stockage.
     */
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("❌ Impossible d'initialiser le stockage !");
        }
    }
}
