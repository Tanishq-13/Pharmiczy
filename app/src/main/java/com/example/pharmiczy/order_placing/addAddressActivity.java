package com.example.pharmiczy.order_placing;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.pharmiczy.OrderConfirmationActivity2;
import com.example.pharmiczy.R;
import com.example.pharmiczy.loginandsignup.RetrofitClient;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class addAddressActivity extends AppCompatActivity implements PaymentResultListener {

    Button save;
    private OrderResponse orderResponse;

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
                        startRazorpayPayment();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("OrderAPI", "API Error Body: " + errorBody);
                            Log.e("OrderAPI", "Response Code: " + response.code());
                            Log.e("OrderAPI", "Message: " + response.message());
                            Toast.makeText(addAddressActivity.this, "Error: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("OrderAPI", "Error parsing errorBody", e);
                            Toast.makeText(addAddressActivity.this, "Unknown error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    Log.e("OrderAPI", "Network/API failure", t);
                    Toast.makeText(addAddressActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private void startRazorpayPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_YourDummyKeyHere"); // Replace with your test key

        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Pharmiczy");
            options.put("description", "Order Payment");
            options.put("currency", "INR");

            // amount in paise (multiply by 100)
            options.put("amount", orderResponse.getOrder().getTotalAmount() * 100);
            options.put("prefill.email", "test@pharmiczy.com");
            options.put("prefill.contact", orderResponse.getOrder().getContact());
            Log.d(TAG, "Starting Razorpay with options: " + options.toString());
            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay payment", e);
            Toast.makeText(this, "Error in starting Razorpay: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();

        // Redirect to order confirmation page
        Intent i = new Intent(this, OrderConfirmationActivity2.class);
        i.putExtra("order_id", orderResponse.getOrder().get_id());
        i.putExtra("order_date", orderResponse.getOrder().getOrderedAt());
        i.putExtra("order_total", String.valueOf(orderResponse.getOrder().getTotalAmount()));
        i.putExtra("order_items", new Gson().toJson(orderResponse.getOrder().getItems())); // Use Gson or serialize properly
        startActivity(i);
        finish();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment failed: " + response, Toast.LENGTH_SHORT).show();
    }

}
