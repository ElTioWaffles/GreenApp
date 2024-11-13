package com.example.trabajo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajo.R;
import com.example.trabajo.model.Ubicacion;

import java.util.List;

public class UbicacionAdapter extends RecyclerView.Adapter<UbicacionAdapter.UbicacionViewHolder> {

    private List<Ubicacion> ubicaciones;

    public UbicacionAdapter(List<Ubicacion> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    @NonNull
    @Override
    public UbicacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ubicacion, parent, false);
        return new UbicacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UbicacionViewHolder holder, int position) {
        Ubicacion ubicacion = ubicaciones.get(position);
        holder.nombreTextView.setText(ubicacion.getNombre());
        holder.descripcionTextView.setText(ubicacion.getDescripcion());
    }

    @Override
    public int getItemCount() {
        return ubicaciones.size();
    }

    static class UbicacionViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView descripcionTextView;

        public UbicacionViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreUbicacionTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionUbicacionTextView);
        }
    }
}
