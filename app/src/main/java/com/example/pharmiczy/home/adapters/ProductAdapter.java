package com.example.pharmiczy.home.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pharmiczy.R;
import com.example.pharmiczy.home.models.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        // Load image using Glide
        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.imageView);

        holder.titleTextView.setText(product.getTitle());
        holder.descriptionTextView.setText(product.getDescription());
        holder.priceTextView.setText(product.getPrice());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView priceTextView,orprice,discp;
        Button addButton;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            titleTextView = itemView.findViewById(R.id.product_title);
            descriptionTextView = itemView.findViewById(R.id.product_quantity);
            priceTextView = itemView.findViewById(R.id.product_price);
            orprice=itemView.findViewById(R.id.product_original_price);
            discp=itemView.findViewById(R.id.product_discount);
            addButton = itemView.findViewById(R.id.add_button);
        }
    }
}
