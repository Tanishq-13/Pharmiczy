package com.example.pharmiczy.home.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pharmiczy.Apis.ApiService;
import com.example.pharmiczy.Apis.requests.CartRequest;
import com.example.pharmiczy.Apis.responses.CartResponse;
import com.example.pharmiczy.DataModels.Medicine;
import com.example.pharmiczy.R;
import com.example.pharmiczy.cache.MedicineCache;
import com.example.pharmiczy.home.adapters.ProductAdapter;
import com.example.pharmiczy.home.models.ProductFetch;
import com.example.pharmiczy.loginandsignup.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImageView;
    private RecyclerView similarProduct;
    private LinearLayoutManager addtocart,buynow;
    private ProductAdapter similarProductAdapter;
    private List<Medicine> similarList = new ArrayList<>();
    private TextView titleTextView, descriptionTextView, priceTextView, originalPriceTextView, discountTextView;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        similarProduct = findViewById(R.id.recycler_view);
        similarProduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        similarProductAdapter = new ProductAdapter(this, similarList);
        similarProduct.setAdapter(similarProductAdapter);

        productImageView = findViewById(R.id.product_image);
        titleTextView = findViewById(R.id.product_title);
        priceTextView = findViewById(R.id.product_price);
        originalPriceTextView = findViewById(R.id.product_original_price);
        discountTextView = findViewById(R.id.product_discount);

        TextView prodn = findViewById(R.id.prodName);
        TextView comp = findViewById(R.id.composition);
        TextView dosage = findViewById(R.id.dosage);
        TextView mino = findViewById(R.id.minOrder);
        TextView packs = findViewById(R.id.packSize);
        TextView dess = findViewById(R.id.Description);
        TextView drugtype = findViewById(R.id.drugType);

        Medicine product = (Medicine) getIntent().getSerializableExtra("product");
        int maxx=product.getStock().quantity;
        EditText editQuantity = findViewById(R.id.edit_quantity);
        TextView warningText =findViewById(R.id.warning_text);

        int maxQuantity = maxx; // Set your dynamic max quantity from backend
        int quantity;

        editQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    int quantity = Integer.parseInt(s.toString());
                    if (quantity > maxQuantity) {
                        warningText.setVisibility(View.VISIBLE);
                    } else {
                        warningText.setVisibility(View.GONE);
                    }
                } catch (NumberFormatException e) {
                    warningText.setVisibility(View.GONE); // hide when field is empty or invalid
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (product != null) {
            // Load image safely
            if (product.getImages() != null && !product.getImages().isEmpty()) {
                Glide.with(this)
                        .load(product.getImages().get(0))
                        .into(productImageView);
            }

            // Safe text population
            if (product.getProductName() != null) {
                titleTextView.setText(product.getProductName());
                prodn.setText(product.getProductName());
            }

            if (product.getDescription() != null) {
                dess.setText(product.getDescription());
            }

            if (product.getDosage() != null) {
                String form = product.getDosage().form != null ? product.getDosage().form : "";
                String strength = product.getDosage().strength != null ? product.getDosage().strength : "";
                dosage.setText(form + ", " + strength);
            }

            if (product.getComposition() != null && product.getComposition().activeIngredients != null) {
                StringBuilder compositionText = new StringBuilder();
                for (String ingredient : product.getComposition().activeIngredients) {
                    if (ingredient != null && !ingredient.isEmpty()) {
                        if (compositionText.length() > 0) compositionText.append(", ");
                        compositionText.append(ingredient);
                    }
                }
                comp.setText(compositionText.toString());
            }

            if (product.getRegulatory() != null && product.getRegulatory().drugType != null) {
                drugtype.setText(product.getRegulatory().drugType);
            }

            if (product.getStock() != null) {
                mino.setText(String.valueOf(product.getStock().minOrderQuantity));
                packs.setText(String.valueOf(product.getStock().quantity));
            }

            if (product.getPricing() != null) {
                priceTextView.setText("₹ " + product.getPricing().mrp);
                originalPriceTextView.setText("₹ " + product.getPricing().sellingPrice);
                originalPriceTextView.setPaintFlags(originalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                discountTextView.setText(product.getPricing().discount + "%");
            }
        }

        // Load similar products from cache
        List<Medicine> cachedList = MedicineCache.getCachedMedicines(this);
        if (cachedList != null && product != null) {
            for (Medicine med : cachedList) {
                if (med.getCategory() != null &&
                        product.getCategory() != null &&
                        med.getCategory().equalsIgnoreCase(product.getCategory()) &&
                        !med.getId().equals(product.getId())) {
                    similarList.add(med);
                }
            }
            similarProductAdapter.notifyDataSetChanged();
        }

        // Handle Add to Cart
        LinearLayout addToCartButton = findViewById(R.id.add_to_cart_btn);
        addToCartButton.setOnClickListener(v -> {
            String token = "Bearer " + getSharedPreferences("pharmiczy_prefs", MODE_PRIVATE).getString("token", null);
            String medicineId = product != null ? product.getId() : null;
//            int quantity = 1;

            if (medicineId == null) {
                Toast.makeText(this, "Invalid medicine", Toast.LENGTH_SHORT).show();
                return;
            }

            CartRequest request = new CartRequest(medicineId, quantity);
            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.addToCart(token, request).enqueue(new Callback<CartResponse>() {
                @Override
                public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProductDetailActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String error = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                            Log.e("AddToCartError", error);
                            Toast.makeText(ProductDetailActivity.this, "Failed to add to cart: " + error, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductDetailActivity.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CartResponse> call, Throwable t) {
                    Toast.makeText(ProductDetailActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Helper method to start ProductDetailActivity with product data
    public static void start(Context context, Medicine product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("product", product);
        context.startActivity(intent);
    }
//    public void onQuantityClick(View view) {
//        // Example: show a simple list dialog for quantity selection
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Select Quantity");
//
//        String[] quantities = {"30 Softgels", "60 Softgels", "90 Softgels"};
//        builder.setItems(quantities, (dialog, which) -> {
//            // Update selected quantity in TextView
//            TextView quantityTextView = findViewById(R.id.selected_quantity);
//            quantityTextView.setText(quantities[which]);
//        });
//
//        builder.show();
//    }
}