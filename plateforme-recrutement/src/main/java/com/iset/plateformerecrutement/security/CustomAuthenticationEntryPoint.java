package com.iset.plateformerecrutement.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper){
        this.objectMapper=objectMapper;
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        Map<String ,Object>errorResponse=new HashMap<>();
        errorResponse.put("error","Unauthorized");
        errorResponse.put("message","Authentication Failed");
        objectMapper.writeValue(response.getWriter(),errorResponse);

    }
}
