package com.example.pharmiczy.home.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pharmiczy.Apis.ApiService;
import com.example.pharmiczy.Apis.requests.RemoveRequest;
import com.example.pharmiczy.Apis.responses.CartResponse;
import com.example.pharmiczy.DataModels.CartItem;
import com.example.pharmiczy.DataModels.Medicine;
import com.example.pharmiczy.R;
import com.example.pharmiczy.loginandsignup.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(List<CartItem> cartItems, Context context) {
        this.cartItems = cartItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.productTitle.setText(item.getMedicine().getProductName());
        holder.productQuantity.setText("Qty: " + item.getQuantity());
        holder.productPrice.setText("₹" + item.getPrice());
        holder.cuttedPrice.setText("₹ " + item.getMedicine().getPricing().mrp);
        SharedPreferences sharedPreferences = context.getSharedPreferences("pharmiczy_prefs", Context.MODE_PRIVATE);
        String token = "Bearer " + sharedPreferences.getString("token", null);

        holder.cuttedPrice.setPaintFlags(holder.cuttedPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.removeItem.setOnClickListener(v -> {

//            String token = "Bearer " + yourTokenHere; // Replace with actual token
            String medicineId = item.getMedicine().getId(); // Get this from your cart item model

            RemoveRequest request = new RemoveRequest(medicineId);
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

            Call<CartResponse> call = apiService.removeFromCart(token, request);

            call.enqueue(new Callback<CartResponse>() {
                @Override
                public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
                        // Optionally update UI or notify adapter
                    } else {
                        Toast.makeText(context, "Failed to remove item", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CartResponse> call, Throwable t) {
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });



    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        LinearLayout removeItem;
        TextView productTitle, productPrice, cuttedPrice, productQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            cuttedPrice = itemView.findViewById(R.id.cutted_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            removeItem=itemView.findViewById(R.id.remove_item_btn);
        }
    }
}
