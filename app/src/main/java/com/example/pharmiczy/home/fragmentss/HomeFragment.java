package com.example.pharmiczy.home.fragmentss;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.example.pharmiczy.DataModels.Medicine;
import com.example.pharmiczy.R;
import com.example.pharmiczy.cache.MedicineCache;
import com.example.pharmiczy.home.activity.AllDoctorsActivity;
import com.example.pharmiczy.home.activity.AllMedicines;
import com.example.pharmiczy.home.adapters.CategoryAdapter;
import com.example.pharmiczy.home.adapters.ProductAdapter;
import com.example.pharmiczy.home.models.Category;
//import com.example.pharmiczy.home.models.Product;
import com.example.pharmiczy.home.models.ProductFetch;
import com.example.pharmiczy.home.productapi;

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
    private RecyclerView productRecyclerView;
    private RecyclerView categoryRecyclerView;
    private LinearLayout ns,clc,crs;

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

        categoryRecyclerView = view.findViewById(R.id.recycler_view2);
        productRecyclerView = view.findViewById(R.id.recycler_view);

        setupCategoryRecyclerView();
        setupProductRecyclerView();

        List<Medicine> cachedList = MedicineCache.getCachedMedicines(getContext());
        if (cachedList != null && !cachedList.isEmpty()) {
            populateFromMedicines(cachedList);
        }

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
        clc=view.findViewById(R.id.calci);

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
