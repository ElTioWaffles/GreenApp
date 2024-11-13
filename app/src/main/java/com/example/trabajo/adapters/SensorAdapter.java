package com.example.trabajo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trabajo.model.Sensor;
import java.util.List;

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorViewHolder> {

    private List<Sensor> sensores;

    public SensorAdapter(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sensor, parent, false);
        return new SensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorViewHolder holder, int position) {
        Sensor sensor = sensores.get(position);
        holder.nombreTextView.setText(sensor.getNombre());
        holder.descripcionTextView.setText(sensor.getDescripcion());
        holder.idealTextView.setText(String.valueOf(sensor.getIdeal()));
    }

    @Override
    public int getItemCount() {
        return sensores != null ? sensores.size() : 0;
    }

    static class SensorViewHolder extends RecyclerView.ViewHolder {
        TextView nombreTextView;
        TextView descripcionTextView;
        TextView idealTextView;

        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreSensorTextView);
            descripcionTextView = itemView.findViewById(R.id.descripcionSensorTextView);
            idealTextView = itemView.findViewById(R.id.idealSensorTextView);
        }
    }
}


