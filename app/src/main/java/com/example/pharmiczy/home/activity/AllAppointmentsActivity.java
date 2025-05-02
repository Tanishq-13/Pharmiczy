package com.example.pharmiczy.home.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmiczy.Apis.ApiService;
import com.example.pharmiczy.Apis.responses.appresp;
import com.example.pharmiczy.R;
import com.example.pharmiczy.home.adapters.BookedAppointmentAdapter;
import com.example.pharmiczy.loginandsignup.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAppointmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookedAppointmentAdapter adapter;
    private List<appresp> appointments = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    public static final String APPOINTMENT_CACHE_KEY = "cached_appointments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_appointments);

        recyclerView = findViewById(R.id.recyclerViewAppointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookedAppointmentAdapter(appointments);
        recyclerView.setAdapter(adapter);

        sharedPreferences = getSharedPreferences("pharmiczy_prefs", MODE_PRIVATE);

        String token = "Bearer " + sharedPreferences.getString("token", null);

        // Try loading cached data first
        loadCachedAppointments();

        // Fetch fresh data
        fetchAppointmentsFromApi(token);
    }

    private void loadCachedAppointments() {
        String cached = sharedPreferences.getString(APPOINTMENT_CACHE_KEY, null);
        if (cached != null) {
            Type type = new TypeToken<List<appresp>>() {}.getType();
            List<appresp> cachedAppointments = new Gson().fromJson(cached, type);
            appointments.clear();
            appointments.addAll(cachedAppointments);
            adapter.notifyDataSetChanged();
        }
    }

    private void fetchAppointmentsFromApi(String token) {
        ApiService appointmentApi = RetrofitClient.getClient().create(ApiService.class);
        appointmentApi.getAppointments(token).enqueue(new Callback<List<appresp>>() {
            @Override
            public void onResponse(Call<List<appresp>> call, Response<List<appresp>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<appresp> fetched = response.body();
                    appointments.clear();
                    appointments.addAll(fetched);
                    adapter.notifyDataSetChanged();

                    // Cache it
                    sharedPreferences.edit()
                            .putString(APPOINTMENT_CACHE_KEY, new Gson().toJson(fetched))
                            .apply();
                }
            }

            @Override
            public void onFailure(Call<List<appresp>> call, Throwable t) {
                Log.e("Appointment API", "Error: " + t.getMessage());
            }
        });
    }
}
