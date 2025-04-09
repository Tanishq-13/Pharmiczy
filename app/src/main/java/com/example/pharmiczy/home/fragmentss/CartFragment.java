package com.example.pharmiczy.home.fragmentss;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.pharmiczy.Apis.ApiClient;
import com.example.pharmiczy.Apis.ApiService;
import com.example.pharmiczy.Apis.responses.AllCartResponse;
import com.example.pharmiczy.Apis.responses.CartResponse;
import com.example.pharmiczy.DataModels.CartItem;
import com.example.pharmiczy.DataModels.Medicine;
import com.example.pharmiczy.R;
import com.example.pharmiczy.home.adapters.CartAdapter;
import com.example.pharmiczy.loginandsignup.RetrofitClient;
import com.example.pharmiczy.order_placing.Address;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private TextView total;
    private Button continu;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cart_items_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CartAdapter(cartItems, getContext());
        recyclerView.setAdapter(adapter);
        total=view.findViewById(R.id.total_cart_amount);
        fetchCartItems();
        continu=view.findViewById(R.id.cart_continue_btn);
        continu.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), Address.class);
            startActivity(intent);
        });
        return view;
    }

    private void fetchCartItems() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("pharmiczy_prefs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getCart(token).enqueue(new Callback<AllCartResponse>() {
            @Override
            public void onResponse(Call<AllCartResponse> call, Response<AllCartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AllCartResponse cartResponse = response.body();
                    Log.d("CartFragment", "Cart fetched. Items: " + cartResponse.getItems().size());

                    cartItems.clear();
                    for (CartResponse item : cartResponse.getItems()) {
                        Medicine med = item.getMedicineId();
                        CartItem cartItem = new CartItem(med, item.getQuantity(), item.getPrice());
                        cartItems.add(cartItem);
                    }
                    total.setText(String.valueOf(cartResponse.getTotalPrice()));
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("CartFragment", "Cart fetch failed. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AllCartResponse> call, Throwable t) {
                Log.e("CartFragment", "API failure: " + t.getMessage());
            }
        });
    }



}