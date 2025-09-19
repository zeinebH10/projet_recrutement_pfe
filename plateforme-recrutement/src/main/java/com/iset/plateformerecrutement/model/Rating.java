      /*  package com.iset.plateformerecrutement.model;


        import com.fasterxml.jackson.annotation.JsonIdentityInfo;
        import com.fasterxml.jackson.annotation.ObjectIdGenerators;
        import jakarta.persistence.*;
        import lombok.*;

        import java.time.LocalDateTime;
        @JsonIdentityInfo(
                generator = ObjectIdGenerators.PropertyGenerator.class,
                property = "id")
        @Entity
        @Getter @Setter
        @NoArgsConstructor @AllArgsConstructor
        public class Rating {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            private int stars;

            @Column(columnDefinition = "TEXT")
            private String commentaire;

            private LocalDateTime dateRating;

            @ManyToOne
            @JoinColumn(nullable = false)
            private Candidat candidat;

            @ManyToOne
            @JoinColumn(nullable = true)
            private Offre offre;

            @ManyToOne
            @JoinColumn(nullable = true)
            private Formation formation;

            @PrePersist
            public void prePersist() {
                this.dateRating = LocalDateTime.now();
            }
        }*/

      package com.iset.plateformerecrutement.model;

      import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
      import jakarta.persistence.*;
      import lombok.*;

      import java.time.LocalDateTime;

      @Entity
      @Getter @Setter
      @NoArgsConstructor @AllArgsConstructor
      @ToString(exclude = {"candidat", "offre", "formation"}) // Éviter les cycles dans toString
      public class Rating {

          @Id
          @GeneratedValue(strategy = GenerationType.IDENTITY)
          private Long id;

          @Column(nullable = false)
          private int stars;

          @Column(columnDefinition = "TEXT")
          private String commentaire;

          @Column(nullable = false)
          private LocalDateTime dateRating;

          @ManyToOne(fetch = FetchType.EAGER)
          @JoinColumn(name = "candidat_id", nullable = false)
          @JsonIgnoreProperties({"ratings", "demandes", "password", "badRatingCount"})
          private Candidat candidat;

          @ManyToOne(fetch = FetchType.EAGER)
          @JoinColumn(name = "offre_id", nullable = true)
          @JsonIgnoreProperties({"ratings", "demandes", "recruteur"})
          private Offre offre;

          @ManyToOne(fetch = FetchType.EAGER)
          @JoinColumn(name = "formation_id", nullable = true)
          @JsonIgnoreProperties({"ratings", "demandes", "formateur"})
          private Formation formation;

          @PrePersist
          public void prePersist() {
              if (this.dateRating == null) {
                  this.dateRating = LocalDateTime.now();
              }
          }

          @PreUpdate
          public void preUpdate() {
              // Optionnel: mettre à jour la date lors de modifications
              // this.dateRating = LocalDateTime.now();
          }

          // Méthode utile pour vérifier la validité
          public boolean isValid() {
              return stars >= 1 && stars <= 5 &&
                      candidat != null &&
                      (offre != null || formation != null) &&
                      !(offre != null && formation != null); // Pas les deux en même temps
          }

          // Méthode pour obtenir le type d'entité notée
          public String getRatedEntityType() {
              if (offre != null) return "OFFRE";
              if (formation != null) return "FORMATION";
              return "UNKNOWN";
          }

          // Méthode pour obtenir l'ID de l'entité notée
          public Long getRatedEntityId() {
              if (offre != null) return offre.getId();
              if (formation != null) return formation.getId();
              return null;
          }
      }
