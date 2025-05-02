package com.example.pharmiczy.home.fragmentss;

import static com.example.pharmiczy.home.activity.AllAppointmentsActivity.APPOINTMENT_CACHE_KEY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmiczy.Apis.ApiClient;
import com.example.pharmiczy.Apis.ApiService;
import com.example.pharmiczy.Apis.responses.AppointmentResponse;
import com.example.pharmiczy.Apis.responses.Doctor;
import com.example.pharmiczy.Apis.responses.appresp;
import com.example.pharmiczy.DataModels.Medicine;
import com.example.pharmiczy.R;
import com.example.pharmiczy.cache.MedicineCache;
import com.example.pharmiczy.home.activity.AllAppointmentsActivity;
import com.example.pharmiczy.home.activity.AllDoctorsActivity;
import com.example.pharmiczy.home.activity.AllMedicines;
import com.example.pharmiczy.home.adapters.CategoryAdapter;
import com.example.pharmiczy.home.adapters.ProductAdapter;
import com.example.pharmiczy.home.adapters.bkdapntmnt;

import com.example.pharmiczy.home.adapters.homedoctoradapter;
import com.example.pharmiczy.home.models.Category;
//import com.example.pharmiczy.home.models.Product;
import com.example.pharmiczy.home.models.ProductFetch;
import com.example.pharmiczy.home.productapi;
import com.example.pharmiczy.loginandsignup.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class HomeFragment extends Fragment {

    private ProductAdapter productAdapter;
    private TextView vwall;
    private RecyclerView productRecyclerView,doctorss,bkdapntmnt;
    private RecyclerView categoryRecyclerView;
    private LinearLayout ns,clc,crs,bkdapnht;
    private bkdapntmnt bkadapter;
    private List<Doctor> doctorList = new ArrayList<>();
    List<appresp> appointments =new ArrayList<>();
    private homedoctoradapter doctorAdapter;
    private final List<Medicine> productList = new ArrayList<>();
    private final List<Category> categoryList = new ArrayList<>();

    private final Set<String> categorySet = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.root_layout), (v, insets) -> {
            Insets systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemInsets.top, 0, systemInsets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        doctorss=view.findViewById(R.id.recycler_view3);
        doctorss.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        doctorAdapter = new homedoctoradapter(doctorList,getContext());
        doctorss.setAdapter(doctorAdapter);
        categoryRecyclerView = view.findViewById(R.id.recycler_view2);
        productRecyclerView = view.findViewById(R.id.recycler_view);
        vwall=view.findViewById(R.id.poo);
        vwall.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(), AllAppointmentsActivity.class);
            startActivity(intent);
        });

        bkdapnht=view.findViewById(R.id.bkdapntmnt);
        bkdapntmnt=view.findViewById(R.id.recycler_view4);
        bkdapntmnt.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        bkadapter = new bkdapntmnt(getContext(),appointments);
        bkdapntmnt.setAdapter(bkadapter);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("pharmiczy_prefs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        ApiService appointmentApi = RetrofitClient.getClient().create(ApiService.class);

        //fetch upcoming appointments
        Call<List<appresp>> call = appointmentApi.getAppointments(token);

        call.enqueue(new Callback<List<appresp>>() {
            @Override
            public void onResponse(Call<List<appresp>> call, Response<List<appresp>> response) {
                if (response.isSuccessful()) {
                    List<appresp> allAppointments = response.body();
                    if (allAppointments != null && !allAppointments.isEmpty()) {
                        List<appresp> limitedAppointments = allAppointments.size() > 2
                                ? allAppointments.subList(0, 2)
                                : allAppointments;
                        appointments.clear();
                        appointments.addAll(limitedAppointments);
                        bkadapter.notifyDataSetChanged();

                        // ✅ Cache the list
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String json = new Gson().toJson(limitedAppointments);
                        editor.putString(APPOINTMENT_CACHE_KEY, json);
                        editor.apply();
                    }

                    if (!appointments.isEmpty()) {
                        bkdapnht.setVisibility(View.VISIBLE);
                        bkdapntmnt.setVisibility(View.VISIBLE);
                    }

                    for (appresp appointment : appointments) {
                        Log.d("Appointment", "Doctor Name: " + appointment.getDoctorId().getName());
                    }
                } else {
                    Log.e("Appointment API", "Failed with code: " + response.code());
                }
            }


            @Override
            public void onFailure(Call<List<appresp>> call, Throwable t) {
                Log.e("Appointment API", "Error: " + t.getMessage());
            }
        });


        setupCategoryRecyclerView();
        setupProductRecyclerView();

        List<Medicine> cachedList = MedicineCache.getCachedMedicines(getContext());
        if (cachedList != null && !cachedList.isEmpty()) {
            populateFromMedicines(cachedList);
        }
        //Fetch doctors
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getAllDoctors().enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    doctorList.clear();
                    doctorList.addAll(response.body());
                    doctorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {
                Log.e("AllDoctors", "Error fetching doctors: " + t.getMessage());
            }
        });


        // Fetch latest medicines
        ApiClient.getApiService().getMedicines().enqueue(new Callback<List<Medicine>>() {
            @Override
            public void onResponse(Call<List<Medicine>> call, Response<List<Medicine>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Medicine> latestData = response.body();
                    MedicineCache.saveMedicines(getContext(), latestData);
                    populateFromMedicines(latestData);
                }
            }

            @Override
            public void onFailure(Call<List<Medicine>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        ns=view.findViewById(R.id.newss);
        crs=view.findViewById(R.id.coursess);
//        clc=view.findViewById(R.id.calci);

        crs.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), AllMedicines.class);
            startActivity(intent);
        });
        ns.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), AllDoctorsActivity.class);
            startActivity(intent);
        });


        return view;
    }

    private void setupCategoryRecyclerView() {
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
    }

    private void setupProductRecyclerView() {
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        productAdapter = new ProductAdapter(getContext(), productList);
        productRecyclerView.setAdapter(productAdapter);
    }

    private void populateFromMedicines(List<Medicine> medicines) {
        productList.clear();
        categoryList.clear();
        categorySet.clear();

        for (Medicine med : medicines) {
            Medicine copiedMed = new Medicine();

            // Basic fields
            copiedMed.setId(med.getId());
            copiedMed.setProductName(med.getProductName());
            copiedMed.setBrandName(med.getBrandName());
            copiedMed.setGenericName(med.getGenericName());
            copiedMed.setManufacturer(med.getManufacturer());
            copiedMed.setDescription(med.getDescription());
            copiedMed.setCategory(med.getCategory());
            copiedMed.setPrescriptionRequired(med.isPrescriptionRequired());

            // Handle images: add placeholder if missing
            List<String> images = (med.getImages() != null && !med.getImages().isEmpty())
                    ? med.getImages()
                    : Collections.singletonList("https://example.com/images/placeholder.jpg");

            copiedMed.setImages(images);


            // Deep copy of nested objects
            copiedMed.setComposition(med.getComposition());
            copiedMed.setDosage(med.getDosage());
            copiedMed.setPricing(med.getPricing());
            copiedMed.setStock(med.getStock());
            copiedMed.setPackaging(med.getPackaging());
            copiedMed.setRegulatory(med.getRegulatory());
            copiedMed.setAdditionalFeatures(med.getAdditionalFeatures());

            productList.add(copiedMed);

            // Unique categories
            if (!categorySet.contains(med.getCategory())) {
                categorySet.add(med.getCategory());
                categoryList.add(new Category(med.getCategory(), R.drawable.shoes1)); // Replace with actual category images
            }
        }

        // Notify adapters
        productAdapter.notifyDataSetChanged();
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

}
