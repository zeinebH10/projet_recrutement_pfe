    package com.iset.plateformerecrutement.Impl;

    import com.iset.plateformerecrutement.model.Formateur;
    import com.iset.plateformerecrutement.model.Recruteur;
    import com.iset.plateformerecrutement.model._Role;
    import com.iset.plateformerecrutement.repository.FormateurRepository;
    import com.iset.plateformerecrutement.requests.Formateur_Register_Request;

    import com.iset.plateformerecrutement.requests.Formateur_Request;
    import com.iset.plateformerecrutement.service.FormateurService;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.List;
    import java.util.Optional;

    @Service
    public class FormateurServiceImpl implements FormateurService {

        private final FormateurRepository formateurRepository;
        private final PasswordEncoder passwordEncoder;
        private final StorageService storageService;

        public FormateurServiceImpl( FormateurRepository formateurRepository, PasswordEncoder passwordEncoder, StorageService storageService) {
            this.formateurRepository = formateurRepository;
            this.passwordEncoder = passwordEncoder;
            this.storageService = storageService;
        }


        @Override
        public List<Formateur> getAllFormateur() {
            return formateurRepository.findAll();
        }

        @Override
        public Optional<Formateur> getformateurById(Long id) {
            return formateurRepository.findById(id);    }

        @Override
        public void deleteFormateur(Long id) {
            formateurRepository.deleteById(id);
        }

        @Override
        public Formateur updateFormateur(Long id, Formateur_Request request, MultipartFile image) throws IOException {
            Optional<Formateur> existingFormateurOpt = formateurRepository.findById(id);
            if (existingFormateurOpt.isEmpty()) {
                throw new RuntimeException("Formateur non trouvé pour ID: " + id);
            }

            Formateur formateur = existingFormateurOpt.get();

            // Mise à jour des champs non nulls
            if (request.getFullName() != null) {
                formateur.setFullName(request.getFullName());
            }
            if (request.getBirthDate() != null) {
                formateur.setBirthDate(request.getBirthDate());
            }
            if (request.getSpecialite()!= null) {
                formateur.setSpecialite(request.getSpecialite());
            }
            if (request.getExperience()!= null) {
                formateur.setExperience(request.getExperience());
            }

            if (request.getDiplome() != null) {
                formateur.setDiplome(request.getDiplome());
            }
            if (request.getTelephone() != null) {
                formateur.setTelephone(request.getTelephone());
            }
            if (request.getCompteLinkedin() != null) {
                formateur.setCompteLinkedin(request.getCompteLinkedin());
            }

            if (request.getAdresse() != null) {
                formateur.setAdresse(request.getAdresse());
            }
            if (request.getNomCentre() != null) {
                formateur.setNomCentre(request.getNomCentre());
            }
            if (request.getRegistreCommerce() != null) {
                formateur.setRegistreCommerce(request.getRegistreCommerce());
            }
            if (request.getSiteWebCentre() != null) {
                formateur.setSiteWebCentre(request.getSiteWebCentre());
            }
            // Gestion de l'image (évite les erreurs 404 et les doublons)
            if (image != null && !image.isEmpty()) {
                String imageName = "formateur_" + id + "_" + image.getOriginalFilename();

                // Vérifier si l'image est identique à l'existante
                if (!imageName.equals(formateur.getImage())) {
                    // Supprimer l'ancienne image si elle existe
                    if (formateur.getImage() != null) {
                        Path oldImagePath = Paths.get("upload-dir").resolve(formateur.getImage());
                        Files.deleteIfExists(oldImagePath);
                    }

                    // Stocker la nouvelle image avec un nom unique si nécessaire
                    storageService.store(image, imageName);
                    formateur.setImage(imageName);
                }
            }

            return formateurRepository.save(formateur);
        }

        @Override
        public void registerFormateur(Formateur_Register_Request request) {
            Optional<Formateur> existingUserByEmail = formateurRepository.findByEmail(request.getEmail());
            Optional<Formateur> existingUserByUsername = formateurRepository.findByUsername(request.getUsername());

            if (existingUserByEmail.isPresent() || existingUserByUsername.isPresent()) {
                throw new RuntimeException("Un utilisateur avec cet email ou ce nom d'utilisateur existe déjà !");
            }

            Formateur formateur =  Formateur.builder()
                    .fullName(request.getFullName())
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .birthDate(request.getBirthDate())
                    .isEnabled(false)
                    .role(_Role.ROLE_FR)
                    .password(passwordEncoder.encode(request.getPassword()))
                    .registreCommerce(request.getRegistreCommerce())
                    .telephone(request.getTelephone())
                    .build();

            Formateur savedFormateur = formateurRepository.save(formateur);
            System.out.println("Formateur après enregistrement : " + savedFormateur);

            formateurRepository.save(formateur);
        }

        @Override
        public void enableFormateur(Long id) {
            Optional<Formateur> formateurOpt = formateurRepository.findById(id);
            if (formateurOpt.isPresent()) {
                Formateur formateur = formateurOpt.get();
                formateur.setEnabled(true);
                formateurRepository.save(formateur);
            } else {
                throw new RuntimeException("Formateur non trouvé pour ID: " + id);
            }
        }

        @Override
        public void disableFormateur(Long id) {
            Optional<Formateur> formateurOpt = formateurRepository.findById(id);
            if (formateurOpt.isPresent()) {
                Formateur formateur = formateurOpt.get();
                formateur.setEnabled(false);
                formateurRepository.save(formateur);
            } else {
                throw new RuntimeException("formateur non trouvé pour ID: " + id);
            }
        }
    }
