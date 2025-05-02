package com.example.pharmiczy;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pharmiczy.Apis.ApiService;
import com.example.pharmiczy.Apis.responses.OrderResponse;
import com.example.pharmiczy.home.adapters.MyOrdersAdapter;
import com.example.pharmiczy.loginandsignup.RetrofitClient;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyOrdersFragment extends Fragment {
    private RecyclerView recyclerViewOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders2, container, false);
        recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchOrders();
        return view;
    }

    private void fetchOrders() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("pharmiczy_prefs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

//        String token = "Bearer " + getSharedPreferences("pharmiczy_prefs", MODE_PRIVATE).getString("token", null);
        // replace with real token
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

        Call<List<OrderResponse.Order>> call = apiService.getMyOrders(token);
        call.enqueue(new Callback<List<OrderResponse.Order>>() {
            @Override
            public void onResponse(Call<List<OrderResponse.Order>> call, Response<List<OrderResponse.Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerViewOrders.setAdapter(new MyOrdersAdapter(response.body()));
                } else {
                    Toast.makeText(getContext(), "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse.Order>> call, Throwable t) {
                Log.e("OrdersAPI", "Fetch error", t);
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
