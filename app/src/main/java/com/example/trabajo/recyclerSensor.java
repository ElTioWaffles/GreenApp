package com.example.trabajo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trabajo.model.Sensor;
import com.example.trabajo.SensorAdapter;
import com.example.trabajo.repositorio.Repositorio;
import java.util.List;

public class recyclerSensor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SensorAdapter sensorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_sensor);

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.recyclerSensor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener la lista de sensores del repositorio
        List<Sensor> sensorList = Repositorio.getInstance().obtenerSensores();

        // Configurar el adaptador y asignarlo al RecyclerView
        sensorAdapter = new SensorAdapter(sensorList);
        recyclerView.setAdapter(sensorAdapter);
    }
}

