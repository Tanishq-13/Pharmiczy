package com.example.pharmiczy.Apis.requests;

public class LoginRequest {
    String email, password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
