package com.example.pharmiczy.home.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
//        titleTextView.setText(prodx);
//        descriptionTextView = findViewById(R.id.product);
        priceTextView = findViewById(R.id.product_price);
        originalPriceTextView = findViewById(R.id.product_original_price);
        discountTextView = findViewById(R.id.product_discount);
        TextView prodn=findViewById(R.id.prodName);
        TextView comp=findViewById(R.id.composition);
        TextView dosage=findViewById(R.id.dosage);
        TextView mino=findViewById(R.id.minOrder);
        TextView packs=findViewById(R.id.packSize);
        TextView dess=findViewById(R.id.Description);
        TextView drudtype=findViewById(R.id.drugType);

//        prodn.setText(p);
        // Get product data from intent
        Medicine product = (Medicine) getIntent().getSerializableExtra("product");
        //populate all fields
        dosage.setText(product.getDosage().form+", "+product.getDosage().strength);
        String h=product.getComposition().activeIngredients.get(0)+", ";
        for(int i=0;i<product.getComposition().activeIngredients.size();i++){
            h=h+product.getComposition().activeIngredients.get(i);
        }
        drudtype.setText(product.getRegulatory().drugType);
        dess.setText(product.getDescription());
        comp.setText(h);
        mino.setText(String.valueOf(product.getStock().minOrderQuantity));
        packs.setText(String.valueOf(product.getStock().quantity));
        prodn.setText(product.getProductName());
        titleTextView.setText(product.getProductName());
        priceTextView.setText("₹ "+product.getPricing().sellingPrice);
        originalPriceTextView.setText("₹ "+product.getPricing().mrp);
        originalPriceTextView.setText("₹ " + product.getPricing().mrp);
        originalPriceTextView.setPaintFlags(originalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        discountTextView.setText(product.getPricing().discount+"%");


        List<Medicine> cachedList = MedicineCache.getCachedMedicines(this);
        if (cachedList != null && product != null) {
            for (Medicine med : cachedList) {
                if (med.getCategory() != null &&
                        med.getCategory().equalsIgnoreCase(product.getCategory()) &&
                        !med.getId().equals(product.getId())) { // exclude the current product

                    similarList.add(med);
                }
            }
            similarProductAdapter.notifyDataSetChanged();
        }




        // Set product details
        if (product != null) {
            Glide.with(this)
                    .load(product.getImages().get(0))
                    .into(productImageView);

            titleTextView.setText(product.getProductName());
//            descriptionTextView.setText(product.getDescription());
            originalPriceTextView.setText("₹" +product.getPricing().sellingPrice);
            discountTextView.setText("("+product.getPricing().discount+"%"+" off)");
            priceTextView.setText("₹" + product.getPricing().mrp);
        }

        LinearLayout addToCartButton = findViewById(R.id.add_to_cart_btn);
        addToCartButton.setOnClickListener(v -> {
            String token = "Bearer " + getSharedPreferences("pharmiczy_prefs", MODE_PRIVATE).getString("token", null);
            Log.d("tokenbearer",token);
            String medicineId = product.getId(); // Replace with actual ID
            int quantity = 53; // Replace with actual quantity

            CartRequest request= new CartRequest(medicineId, quantity);
            ApiService api = RetrofitClient.getClient().create(ApiService.class);
            api.addToCart(token, request).enqueue(new Callback<CartResponse>() {
                @Override
                public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ProductDetailActivity.this, "Added to Cart!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.errorBody() != null) {
                            try {
                                String errorResponse = response.errorBody().string();
                                Log.e("AddToCartError", errorResponse); // Log the detailed error
                                Toast.makeText(ProductDetailActivity.this,
                                        "Failed to add to cart.\nServer response: " + errorResponse,
                                        Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(ProductDetailActivity.this,
                                        "Failed to add to cart. Could not parse error response.",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(ProductDetailActivity.this,
                                    "Failed to add to cart. Unknown error occurred.",
                                    Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(ProductDetailActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
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