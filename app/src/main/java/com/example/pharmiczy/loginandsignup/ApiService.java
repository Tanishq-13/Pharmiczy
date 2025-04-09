package com.example.pharmiczy.loginandsignup;

import com.example.pharmiczy.Apis.responses.LoginResponse;
import com.example.pharmiczy.loginandsignup.datamodels.LoginRequest;
import com.example.pharmiczy.loginandsignup.datamodels.SignupRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/api/user/register")  // Endpoint of the signup API
    Call<Void> signup(@Body SignupRequest request);  // No response body expected
    @Headers("Content-Type: application/json")

    @POST("/api/auth/login") // Adjust based on your backend route
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);
}

