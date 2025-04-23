package com.example.pharmiczy.order_placing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pharmiczy.Apis.ApiClient;
import com.example.pharmiczy.Apis.ApiService;
import com.example.pharmiczy.Apis.requests.PlaceOrderRequest;
import com.example.pharmiczy.Apis.responses.OrderResponse;
import com.example.pharmiczy.R;
import com.example.pharmiczy.loginandsignup.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addAddressActivity extends AppCompatActivity {

    Button save;
    EditText city, locality, flatNo, pincode, state, name, mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_address);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize fields
        save = findViewById(R.id.save_btn);
        city = findViewById(R.id.city);
        locality = findViewById(R.id.locality);
        flatNo = findViewById(R.id.flat_no);
        pincode = findViewById(R.id.pincode);
        state = findViewById(R.id.state);
        name = findViewById(R.id.name);
        mobileNo = findViewById(R.id.mobile_no);

        save.setOnClickListener(v -> {
            String address = flatNo.getText().toString().trim() + ", " +
                    locality.getText().toString().trim() + ", " +
                    city.getText().toString().trim() + ", " +
                    state.getText().toString().trim() + " - " +
                    pincode.getText().toString().trim();

            String contact = mobileNo.getText().toString().trim();

            if (address.isEmpty() || contact.isEmpty()) {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the request
            PlaceOrderRequest request = new PlaceOrderRequest(address, contact);

            // Call the API
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("pharmiczy_prefs", Context.MODE_PRIVATE);
            String token = "Bearer " + sharedPreferences.getString("token", null);
           ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            Call<OrderResponse> call = apiService.placeOrder(token, request);

            call.enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(addAddressActivity.this, "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(addAddressActivity.this, Address.class));
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Toast.makeText(addAddressActivity.this, "Error: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(addAddressActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    Toast.makeText(addAddressActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
