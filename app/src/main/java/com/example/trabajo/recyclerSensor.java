package com.example.trabajo;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trabajo.model.Sensor;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class recyclerSensor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SensorAdapter sensorAdapter;
    private List<Sensor> listaSensores;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_sensor);

        // Inicialización de Firebase y la lista de sensores
        db = FirebaseFirestore.getInstance();
        listaSensores = new ArrayList<>();

        // Configuración del RecyclerView
        recyclerView = findViewById(R.id.recyclerSensor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configuración del adaptador
        sensorAdapter = new SensorAdapter(listaSensores);
        recyclerView.setAdapter(sensorAdapter);

        // Cargar datos desde Firestore
        cargarSensores();
    }

    private void cargarSensores() {
        db.collection("sensores")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaSensores.clear(); // Limpia la lista para evitar duplicados
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Sensor sensor = document.toObject(Sensor.class);
                            listaSensores.add(sensor); // Agrega cada sensor a la lista
                        }
                        sensorAdapter.notifyDataSetChanged(); // Notifica al adaptador los cambios
                    } else {
                        Toast.makeText(this, "Error al cargar los sensores", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

