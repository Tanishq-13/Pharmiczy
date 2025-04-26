package com.example.pharmiczy.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmiczy.R;
import com.example.pharmiczy.Apis.responses.appresp;
import com.example.pharmiczy.home.activity.AppointmentActivity;

import java.util.List;

public class bkdapntmnt extends RecyclerView.Adapter<bkdapntmnt.bkdapntmntviewholder> {

    private Context context;
    private List<appresp> appointmentList;

    public bkdapntmnt(Context context, List<appresp> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public bkdapntmntviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booked_appointment, parent, false);
        return new bkdapntmntviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull bkdapntmntviewholder holder, int position) {
        appresp appointment = appointmentList.get(position);

        holder.textDoctorName.setText(" Dr. " + appointment.getDoctorId().getName());
        holder.textDate.setText(" "+appointment.getDate());
        holder.textTime.setText(" "+appointment.getTime());

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AppointmentActivity.class);
            intent.putExtra("doctorName", appointment.getDoctorId().getName());
            intent.putExtra("specialization", appointment.getDoctorId().getSpecialization());
            intent.putExtra("contact", appointment.getDoctorId().getContact());
            intent.putExtra("date", appointment.getDate());
            intent.putExtra("time", appointment.getTime());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class bkdapntmntviewholder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textDoctorName, textDate, textTime;

        public bkdapntmntviewholder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            textDoctorName = itemView.findViewById(R.id.textDoctorName);
            textDate = itemView.findViewById(R.id.textDate);
            textTime = itemView.findViewById(R.id.textTime);
        }
    }
}
