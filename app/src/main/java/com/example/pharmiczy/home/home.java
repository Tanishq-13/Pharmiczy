package com.example.pharmiczy.home;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmiczy.Apis.ApiClient;
import com.example.pharmiczy.DataModels.Medicine;
import com.example.pharmiczy.R;
import com.example.pharmiczy.home.adapters.CategoryAdapter;
import com.example.pharmiczy.home.adapters.ProductAdapter;
import com.example.pharmiczy.home.models.Category;
import com.example.pharmiczy.home.models.Product;
import com.example.pharmiczy.home.models.ProductFetch;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class home extends AppCompatActivity {

    private static final String BASE_URL = "https://pharma-ecommerce.onrender.com/api/product/";
    private ProductAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        // Enable light status bar for dark icons
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Handle insets to adjust padding for status and navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root_layout), (v, insets) -> {
            Insets systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemInsets.top, 0, systemInsets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        //category griditem
        RecyclerView recyclerView2 = findViewById(R.id.recycler_view2);

        // Set up GridLayoutManager with 4 columns
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 4));

        // Sample data
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Hair Care", R.drawable.shoes1));
        categoryList.add(new Category("Oral Care", R.drawable.shoes1));
        categoryList.add(new Category("Sexual Wellness", R.drawable.shoes1));
        categoryList.add(new Category("Skin Care", R.drawable.shoes1));
        categoryList.add(new Category("Feminine Care",R.drawable.shoes1));
        categoryList.add(new Category("Baby Care", R.drawable.shoes1));
        categoryList.add(new Category("Elderly Care", R.drawable.shoes1));
        categoryList.add(new Category("Men Grooming", R.drawable.shoes1));

        // Set up the adapter
        CategoryAdapter adapter = new CategoryAdapter(this, categoryList);
        recyclerView2.setAdapter(adapter);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize Retrofit and fetch products

        fetchProducts();
    }

    private void fetchProducts() {
//        Call<List<Product>> call = apiService.getProducts();

        ApiClient.getApiService().getMedicines().enqueue(new Callback<List<Medicine>>() {
            @Override
            public void onResponse(Call<List<Medicine>> call, Response<List<Medicine>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Medicine> productList = response.body();
                    // Set up the adapter with fetched data
                    for (Medicine product : productList) {
                        Log.d("Product", "Name: " + product.getProductName() + ", Price: " + product.getPricing().sellingPrice);
                    }
                    adapter = new ProductAdapter(home.this, productList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("Error", "Failed to retrieve products.");
                }
            }

            @Override
            public void onFailure(Call<List<Medicine>> call, Throwable t) {

            }

        });
    }
}
