package com.example.pharmiczy.home;

import com.example.pharmiczy.home.models.ProductFetch;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface productapi {
    @GET("products")
    Call<List<ProductFetch>> getProducts();
}
