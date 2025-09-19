package com.iset.plateformerecrutement.controller;


import com.iset.plateformerecrutement.model._User;
import com.iset.plateformerecrutement.requests.*;
import com.iset.plateformerecrutement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/distribution")
    public Map<String, Long> getUserDistribution() {
        return userService.getUserCountsByRole();
    }

    @GetMapping("/me")
    public ResponseEntity<_User> getCurrentUser() {
        _User currentUser = userService.getUserConnecte();
        return ResponseEntity.ok(currentUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
            JwtToken jwtToken =userService.Login(loginRequest);
            return ResponseEntity.ok(jwtToken);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.register(registerRequest);
            return ResponseEntity.ok(new SuccessMessageRequest("User registered successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @GetMapping("/all")
    public ResponseEntity<List<_User>> getAllUsers() {
        List<_User> users = userService.getUser();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<_User> getUserById(@PathVariable int id) {
        try {
            _User user = userService.getById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            _User user = userService.getByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }


    @PostMapping("/auth/send-token")
    public ResponseEntity<?> sendToken(@RequestParam String email) {
        try {
            userService.sendToken(email);
            return ResponseEntity.ok(new SuccessMessageRequest("Token sent successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/auth/verify-token")
    public ResponseEntity<?> verifiedToken(@RequestBody VerifiedTokenRequest verifTokenRequest) {
        try {
            userService.verifiedToken(verifTokenRequest);
            return ResponseEntity.ok(new SuccessMessageRequest("Token verified successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        try {
            userService.resetPassword(resetPasswordRequest);
            return ResponseEntity.ok(new SuccessMessageRequest("Password reset successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
