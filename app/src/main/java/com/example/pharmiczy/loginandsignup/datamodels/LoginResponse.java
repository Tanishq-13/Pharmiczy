package com.example.pharmiczy.loginandsignup.datamodels;

public class LoginResponse {
    private String message;
    private String token; // Assuming backend returns JWT token

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
}

