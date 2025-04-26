package com.example.pharmiczy.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmiczy.Apis.responses.Doctor;
import com.example.pharmiczy.R;
import com.example.pharmiczy.home.activity.BookAppointmentActivity;

import java.util.List;

public class homedoctoradapter extends RecyclerView.Adapter<homedoctoradapter.homeDoctorViewHolder> {
    private List<Doctor> doctorList;
    private Context context;

    public homedoctoradapter(List<Doctor> doctorList, Context context) {
        this.doctorList = doctorList;
        this.context = context;
    }

    @NonNull
    @Override
    public homeDoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.doctorcard, parent, false);
        return new homeDoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull homeDoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        holder.doctorName.setText(doctor.getName());
//        holder.specialization.setText(doctor.getSpecialization());
//        holder.contact.setText(doctor.getContact());
        holder.email.setText(doctor.getEmail());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookAppointmentActivity.class);
            intent.putExtra("doctor", doctor);
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class homeDoctorViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName,email;

        public homeDoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
//            specialization = itemView.findViewById(R.id.calculatorDescription);
            email=itemView.findViewById(R.id.email);
//            contact=itemView.findViewById(R.id.contact);
        }
    }
}
