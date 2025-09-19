    package com.iset.plateformerecrutement.model;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.SuperBuilder;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.Collection;
    import java.util.List;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    @Inheritance(strategy = InheritanceType.JOINED)
    public class _User implements UserDetails {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        private String fullName;
        private String email;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate birthDate;
        private String username;
        private String password;
        private boolean isEnabled;

        @Enumerated(value = EnumType.STRING)
        private _Role role;

        private LocalDateTime registrationDate;

        @OneToOne(mappedBy = "user")
        private _Token token;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority(role.name()));
        }

        @Override public boolean isAccountNonExpired() { return true; }
        @Override public boolean isAccountNonLocked() { return true; }
        @Override public boolean isCredentialsNonExpired() { return true; }
        @Override public boolean isEnabled() { return isEnabled; }

        @PrePersist
        protected void onCreate() {
            this.registrationDate = LocalDateTime.now();
        }
    }
