package com.example.pharmiczy.Apis;


import com.example.pharmiczy.Apis.requests.AppointmentRequest;
import com.example.pharmiczy.Apis.requests.CartRequest;
import com.example.pharmiczy.Apis.requests.PlaceOrderRequest;
import com.example.pharmiczy.Apis.responses.AllCartResponse;
import com.example.pharmiczy.Apis.responses.AppointmentResponse;
import com.example.pharmiczy.Apis.responses.CartResponse;
import com.example.pharmiczy.Apis.responses.Doctor;
import com.example.pharmiczy.Apis.responses.OrderResponse;
import com.example.pharmiczy.DataModels.Medicine;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @GET("medicines/")
    Call<List<Medicine>> getMedicines();
    @GET("medicines/{id}")
    Call<Medicine> getMedicineById(@Path("id") String medicineId);
    @POST("api/carts/add")
    Call<CartResponse> addToCart(@Header("Authorization") String token, @Body CartRequest request);

    @GET("api/carts/")
    Call<AllCartResponse> getCart(@Header("Authorization") String token);

    @GET("/api/auth/doctors")
    Call<List<Doctor>> getAllDoctors();
    @POST("/api/appointments")
    Call<AppointmentResponse> bookAppointment(
            @Header("Authorization") String token,
            @Body AppointmentRequest request
    );

    @POST("/api/orders/place")
    Call<OrderResponse> placeOrder(@Header("Authorization") String token, @Body PlaceOrderRequest request);
    @POST("api/carts/remove")
    Call<CartResponse> removeFromCart(
            @Header("Authorization") String token,
            @Body CartRequest request
    );

}
