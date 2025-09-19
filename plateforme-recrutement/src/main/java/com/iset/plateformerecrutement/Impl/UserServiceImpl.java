package com.iset.plateformerecrutement.Impl;



import com.iset.plateformerecrutement.model._Role;
import com.iset.plateformerecrutement.model._Token;
import com.iset.plateformerecrutement.model._User;
import com.iset.plateformerecrutement.repository.TokenRepository;
import com.iset.plateformerecrutement.repository.UserRepository;
import com.iset.plateformerecrutement.requests.*;
import com.iset.plateformerecrutement.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;
    private  final JwtEncoder jwtEncoder;
    private final TokenRepository tokenRepository;


    public Map<String, Long> getUserCountsByRole() {
        Map<String, Long> counts = new HashMap<>();

        // Conversion des chaînes de caractères en éléments de l'énumération
        counts.put("candidat", userRepository.countByRole(_Role.valueOf("ROLE_CANDIDAT")));
        counts.put("recruteur", userRepository.countByRole(_Role.valueOf("ROLE_RH")));
        counts.put("formateur", userRepository.countByRole(_Role.valueOf("ROLE_FR")));

        return counts;
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("This email is already in use.");
        }
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("This username has been taken.");
        }

        _User user = _User.builder()
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .birthDate(registerRequest.getBirthDate())
                .role(_Role.ROLE_USER)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .isEnabled(true)
                .build();
        userRepository.save(user);
    }

    @Override
    public JwtToken Login(LoginRequest loginRequest) {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));
            return new JwtToken(createToken(auth));

        } catch (DisabledException e) {
            throw new IllegalArgumentException("The account has been disabled");
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Incorrect credentials");
        } catch (Exception e) {
            throw new RuntimeException("Incorrect credentials");
        }
    }



    @Override
    public List<_User> getUser() {
        return userRepository.findAll().stream().filter(user->user.getRole().equals(_Role.ROLE_USER)).toList();
    }

    @Override
    public _User getById(int id) {
        return null;
    }


    @Override
    public _User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("user not found"));
    }
    private String createToken(Authentication authentication) {
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 300))
                .subject(authentication.getName())
                .claim("scope", createScope(authentication))
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(claims);
        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

    private String createScope(Authentication authentication) {
        return authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.joining(" "));
    }

    private String generateToken() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private Date getExpirationDate() {
        return new Date(System.currentTimeMillis() + 1000 * 60 * 10);
    }
    @Override
    public void createAdmin(RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isEmpty()
                && userRepository.findByEmail(registerRequest.getEmail()).isEmpty()) {
            _User user = _User.builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .isEnabled(true)
                    .role(_Role.ROLE_ADMIN)
                    .build();
            userRepository.save(user);
        }
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        _User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("there is no user with this email!"));
        _Token token = tokenRepository.findByUserEmail(request.getEmail());

        if (request.getNewPassword().equals(request.getConfirmPassword()) &&
                request.getTokenText().equals(token.getTokenText())) {

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            tokenRepository.deleteById(token.getId_token());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong");
        }
    }


    @Override
    public void sendToken(String email) {
        Optional<_User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String tokenText = this.generateToken();
            _Token existingToken = tokenRepository.findByUserEmail(email);
            if (existingToken != null) {
                tokenRepository.deleteById(existingToken.getId_token());
            }
            _Token tokenToSave = new _Token();
            tokenToSave.setTokenText(tokenText);
            tokenToSave.setExpiryDate(this.getExpirationDate());
            tokenToSave.setUser(user.get());
            _Token token = tokenRepository.save(tokenToSave);
            if (token == null) {
                throw new RuntimeException("Failed to save token");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such user with provided email");
        }
    }




    @Override
    public void verifiedToken(VerifiedTokenRequest request) {
        Date dateNow = new Date(System.currentTimeMillis());
        _Token token = tokenRepository.findByUserEmail(request.getEmail());
        Date expDateCurrentToken = token.getExpiryDate();
        int result = dateNow.compareTo(expDateCurrentToken);
        if (result > 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The link has expired.");
        }
        if (!request.getTokenText().equals(token.getTokenText())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong token provided");
        }
    }

    @Override
           public _User getUserConnecte() {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
                throw new IllegalStateException("Aucun utilisateur connecté");
            }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
    }

}
