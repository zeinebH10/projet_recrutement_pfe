package com.iset.plateformerecrutement.service;


import com.iset.plateformerecrutement.model._User;
import com.iset.plateformerecrutement.requests.*;

import java.util.List;
import java.util.Map;

public interface UserService {
    Map<String, Long> getUserCountsByRole();
    void register(RegisterRequest registerRequest);

    JwtToken Login(LoginRequest loginRequest);
    _User getUserConnecte();
    List<_User> getUser();
    _User getById(int id);
    _User getByUsername(String username);
    void createAdmin(RegisterRequest registerRequest);
    void verifiedToken(VerifiedTokenRequest request);
    void sendToken(String email);
    void resetPassword(ResetPasswordRequest request);

}