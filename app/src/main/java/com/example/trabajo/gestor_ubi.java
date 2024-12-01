package com.example.trabajo;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajo.adapters.UbicacionAdapter;
import com.example.trabajo.model.Ubicacion;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class gestor_ubi extends AppCompatActivity {
    private EditText ubiSensorEditText;
    private EditText descripcionEditText;
    private Button ingresarUbiButton;
    private RecyclerView recyclerUbicaciones;
    private UbicacionAdapter ubicacionAdapter;
    private List<Ubicacion> ubicaciones;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor_ubi);

        db = FirebaseFirestore.getInstance();

        // Inicialización de vistas
        ubiSensorEditText = findViewById(R.id.UbiSensor2);
        descripcionEditText = findViewById(R.id.descripc);
        ingresarUbiButton = findViewById(R.id.ingresarUbiButton);
        recyclerUbicaciones = findViewById(R.id.recyclerUbicaciones);

        // Configuración inicial del RecyclerView
        ubicaciones = new ArrayList<>();
        ubicacionAdapter = new UbicacionAdapter(ubicaciones);
        recyclerUbicaciones.setLayoutManager(new LinearLayoutManager(this));
        recyclerUbicaciones.setAdapter(ubicacionAdapter);

        // Cargar ubicaciones desde Firestore
        cargarUbicaciones();

        // Configuración del botón para agregar ubicaciones
        ingresarUbiButton.setOnClickListener(v -> agregarUbicacion());
    }

    private void cargarUbicaciones() {
        db.collection("ubicaciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ubicaciones.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Ubicacion ubicacion = document.toObject(Ubicacion.class);
                            ubicaciones.add(ubicacion);
                        }
                        ubicacionAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar ubicaciones", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void agregarUbicacion() {
        String nombre = ubiSensorEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();

        if (!validarCampos(nombre, descripcion)) return;

        // Crear la nueva ubicación
        Ubicacion nuevaUbicacion = new Ubicacion(nombre, descripcion);

        // Guardar en Firestore
        db.collection("ubicaciones")
                .add(nuevaUbicacion)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Ubicación guardada correctamente", Toast.LENGTH_SHORT).show();
                    ubicaciones.add(nuevaUbicacion);
                    ubicacionAdapter.notifyItemInserted(ubicaciones.size() - 1);
                    limpiarCampos();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar la ubicación", Toast.LENGTH_SHORT).show());
    }

    private boolean validarCampos(String nombre, String descripcion) {
        if (TextUtils.isEmpty(nombre) || nombre.length() < 5 || nombre.length() > 15 || nombre.contains(" ")) {
            Toast.makeText(this, "El nombre es obligatorio y debe tener entre 5 y 15 caracteres sin espacios", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (descripcion.length() > 30) {
            Toast.makeText(this, "La descripción no debe exceder los 30 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        ubiSensorEditText.setText("");
        descripcionEditText.setText("");
    }
}