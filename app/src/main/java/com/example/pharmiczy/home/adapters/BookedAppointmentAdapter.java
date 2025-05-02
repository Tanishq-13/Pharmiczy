package com.example.pharmiczy.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pharmiczy.Apis.responses.appresp;
import com.example.pharmiczy.R;

import java.util.List;

public class BookedAppointmentAdapter extends RecyclerView.Adapter<BookedAppointmentAdapter.ViewHolder> {

    private List<appresp> appointments;

    public BookedAppointmentAdapter(List<appresp> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booked_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        appresp item = appointments.get(position);

        holder.textDoctorName.setText("Dr. " + item.getDoctorId().getName());
        holder.textDate.setText(item.getDate());
        holder.textTime.setText(item.getTime());
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDoctorName, textDate, textTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDoctorName = itemView.findViewById(R.id.textDoctorName);
            textDate = itemView.findViewById(R.id.textDate);
            textTime = itemView.findViewById(R.id.textTime);
        }
    }
}

