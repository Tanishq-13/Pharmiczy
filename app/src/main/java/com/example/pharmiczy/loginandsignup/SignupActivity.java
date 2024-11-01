package com.example.pharmiczy.loginandsignup;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pharmiczy.R;
import com.example.pharmiczy.loginandsignup.datamodels.SignupRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private EditText username, useremail, userpassword, usercnfpassword;
    private TextView su;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        username = findViewById(R.id.username);
        useremail = findViewById(R.id.useremail);
        userpassword = findViewById(R.id.userpassword);
        usercnfpassword = findViewById(R.id.usercnfpassword);
        signupButton = findViewById(R.id.continue_button);
        su=findViewById(R.id.signup);
        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),loginactivity.class);
                startActivity(it);
            }
        });
        signupButton.setOnClickListener(v -> {
            String name = username.getText().toString();
            String email = useremail.getText().toString();
            String password = userpassword.getText().toString();
            String confirmPassword = usercnfpassword.getText().toString();

            if (validateInputs(name, email, password, confirmPassword)) {
                signupUser(name, email, password);
            }
        });
    }

    private boolean validateInputs(String name, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

//        if (!password.equals(confirmPassword)) {
//            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//            return false;
//        }
        return true;
    }

    private void signupUser(String name, String email, String password) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        SignupRequest request = new SignupRequest(name, email, password, "123450");  // Example phone number

        Call<Void> call = apiService.signup(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("SignupActivity", "Response Code: " + response.code());
                Log.d("SignupActivity", "Response Message: " + response.message());
                if (response.isSuccessful()) {
                    Toast.makeText(SignupActivity.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignupActivity.this, "Signup Failed2: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("SignupActivity", "Error: " + t.getMessage());
                Toast.makeText(SignupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}