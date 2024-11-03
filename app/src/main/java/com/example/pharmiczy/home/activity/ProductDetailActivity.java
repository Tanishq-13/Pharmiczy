package com.example.pharmiczy.home.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.pharmiczy.R;
import com.example.pharmiczy.home.models.ProductFetch;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImageView;
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
        productImageView = findViewById(R.id.product_image);
        titleTextView = findViewById(R.id.product_title);
//        descriptionTextView = findViewById(R.id.product);
        priceTextView = findViewById(R.id.product_price);
        originalPriceTextView = findViewById(R.id.product_original_price);
        discountTextView = findViewById(R.id.product_discount);

        // Get product data from intent
        ProductFetch product = (ProductFetch) getIntent().getSerializableExtra("product");

        // Set product details
        if (product != null) {
            Glide.with(this)
                    .load(product.getImage())
                    .into(productImageView);

            titleTextView.setText(product.getName());
//            descriptionTextView.setText(product.getDescription());
            originalPriceTextView.setText("₹" +product.getOriginalPrice());
            discountTextView.setText(""+product.getDiscount()+"%");
            priceTextView.setText("₹" + product.getPrice());
        }
    }

    // Helper method to start ProductDetailActivity with product data
    public static void start(Context context, ProductFetch product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra("product", product);
        context.startActivity(intent);
    }
    public void onQuantityClick(View view) {
        // Example: show a simple list dialog for quantity selection
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Quantity");

        String[] quantities = {"30 Softgels", "60 Softgels", "90 Softgels"};
        builder.setItems(quantities, (dialog, which) -> {
            // Update selected quantity in TextView
            TextView quantityTextView = findViewById(R.id.selected_quantity);
            quantityTextView.setText(quantities[which]);
        });

        builder.show();
    }
}