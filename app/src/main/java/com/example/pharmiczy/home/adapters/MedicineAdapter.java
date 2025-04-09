package com.example.pharmiczy.home.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmiczy.DataModels.Medicine;
import com.example.pharmiczy.R;
import com.example.pharmiczy.home.activity.ProductDetailActivity;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private List<Medicine> medicineList;
    private Context context;

    public MedicineAdapter(List<Medicine> medicineList, Context context) {
        this.medicineList = medicineList;
        this.context = context;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.name.setText(medicine.getProductName());
        holder.sellingPrice.setText("₹" + medicine.getPricing().sellingPrice);
        holder.originalPrice.setText("₹" + medicine.getPricing().mrp);
        holder.itemView.setOnClickListener(v -> ProductDetailActivity.start(context, medicine));

        // If you're loading image from URL use Glide or Picasso
        // For now using placeholder
        holder.image.setImageResource(R.drawable.medicine_9_svgrepo_com_3);
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, originalPrice, sellingPrice;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.medicine_image);
            name = itemView.findViewById(R.id.medicine_name);
            originalPrice = itemView.findViewById(R.id.medicine_original_price);
            sellingPrice = itemView.findViewById(R.id.medicine_selling_price);
        }
    }
}

