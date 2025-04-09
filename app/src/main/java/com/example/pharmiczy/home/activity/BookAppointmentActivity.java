package com.example.pharmiczy.home.activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pharmiczy.Apis.ApiClient;
import com.example.pharmiczy.Apis.ApiService;
import com.example.pharmiczy.Apis.requests.AppointmentRequest;
import com.example.pharmiczy.Apis.responses.AppointmentResponse;
import com.example.pharmiczy.Apis.responses.Doctor;
import com.example.pharmiczy.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookAppointmentActivity extends AppCompatActivity {
    private String selectedDate = "";
    private String selectedTime = "";

    private TextView doctorName, specialization, email, contact;
    private CalendarView appointmentDate;
    private TimePicker appointmentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        doctorName = findViewById(R.id.doctorName);
        specialization = findViewById(R.id.doctorSpecialization);
        email = findViewById(R.id.doctorEmail);
        contact = findViewById(R.id.doctorContact);
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentTime = findViewById(R.id.appointmentTime);
        Button bookAppointmentBtn = findViewById(R.id.bookAppointmentBtn);

        Doctor doctor = (Doctor) getIntent().getSerializableExtra("doctor");
        if (doctor != null) {
            doctorName.setText(doctor.getName());
            specialization.setText("Specialization: " + doctor.getSpecialization());
            email.setText("Email: " + doctor.getEmail());
            contact.setText("Contact: " + doctor.getContact());
        }

        // Date Selection
        appointmentDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDate = sdf.format(calendar.getTime());
        });

        bookAppointmentBtn.setOnClickListener(v -> {
            if (selectedDate.isEmpty()) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected time
            int hour = appointmentTime.getHour();
            int minute = appointmentTime.getMinute();
            selectedTime = String.format(Locale.getDefault(), "%02d:%02d %s",
                    (hour % 12 == 0 ? 12 : hour % 12), minute, (hour < 12 ? "AM" : "PM"));

            // Get token and userId
            SharedPreferences prefs = getSharedPreferences("pharmiczy_prefs", MODE_PRIVATE);
            String token = prefs.getString("token", null);
            String patientId = prefs.getString("userId", null);

            // Prepare request
            AppointmentRequest appointmentRequest = new AppointmentRequest(
                    patientId,
                    doctor.get_id(),
                    selectedDate,
                    selectedTime
            );

            ApiService apiService = ApiClient.getApiService();
            Call<AppointmentResponse> call = apiService.bookAppointment("Bearer " + token, appointmentRequest);

            call.enqueue(new Callback<AppointmentResponse>() {
                @Override
                public void onResponse(Call<AppointmentResponse> call, Response<AppointmentResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        new android.app.AlertDialog.Builder(BookAppointmentActivity.this)
                                .setTitle("Success")
                                .setMessage(response.body().getMessage())
                                .setPositiveButton("OK", (dialog, which) -> finish())
                                .show();
                    } else {
                        Toast.makeText(BookAppointmentActivity.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AppointmentResponse> call, Throwable t) {
                    Toast.makeText(BookAppointmentActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
