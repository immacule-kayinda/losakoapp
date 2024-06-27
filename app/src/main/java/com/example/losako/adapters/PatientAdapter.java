package com.example.losako.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.losako.MainActivity;
import com.example.losako.R;
import com.example.losako.models.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {
    private final ArrayList<Patient> patients;
    private final OnItemClickListener itemClickListener;
    private final Context context;

    public PatientAdapter(Context context, ArrayList<Patient> patients, OnItemClickListener listener) {
        this.context = context;
        this.patients = patients;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.patient_item, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);
        holder.tvNom.setText(patient.getNomPatient());
        holder.tvPostNom.setText(patient.getPostnomPatient());
        holder.tvPrenom.setText(patient.getPrenomPatient());
        holder.tvMaladie.setText(patient.getMaladiePatient());
        holder.tvDateVenu.setText(patient.getDateVenuPatient());

        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(v, position));

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public void setFilteredData(List<Patient> newPatients) {
        this.patients.clear();
        this.patients.addAll(newPatients);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvPostNom, tvPrenom, tvMaladie, tvDateVenu;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tvNom);
            tvPostNom = itemView.findViewById(R.id.tvPostNom);
            tvPrenom = itemView.findViewById(R.id.tvPrenom);
            tvMaladie = itemView.findViewById(R.id.tvMaladie);
            tvDateVenu = itemView.findViewById(R.id.tvDateVenu);
        }
    }
}
