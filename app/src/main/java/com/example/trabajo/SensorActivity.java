package com.example.trabajo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStructure;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.trabajo.model.Ubicacion;
import com.example.trabajo.model.Tipo;
import com.example.trabajo.model.Sensor;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private TextView resultadoBusquedaTextView;
    private Spinner spinnerUbicacion, spinnerTipoSensor;
    private EditText nombreEditText, descripcionEditText, idealEditText;
    private ArrayAdapter<String> ubicacionAdapter, tipoAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        db = FirebaseFirestore.getInstance();

        // Inicialización de vistas
        spinnerTipoSensor = findViewById(R.id.SpinnerSensor);
        spinnerUbicacion = findViewById(R.id.UbiSensor);
        nombreEditText = findViewById(R.id.sensores);
        descripcionEditText = findViewById(R.id.modeloSensorEditText);
        idealEditText = findViewById(R.id.campoIdealEditText);
        resultadoBusquedaTextView = findViewById(R.id.resultadoBusquedaTextView);


        // Configuración de Spinners
        configurarSpinners();

        // Configuración de botones
        Button verSensoresButton = findViewById(R.id.verSensoresButton);
        verSensoresButton.setOnClickListener(v -> {
            Intent intent = new Intent(SensorActivity.this, recyclerSensor.class);
            startActivity(intent);
        });

        Button ingresarSensorButton = findViewById(R.id.ingresarSensorButton);
        ingresarSensorButton.setOnClickListener(v -> agregarSensor());

        Button modificarSensorButton = findViewById(R.id.ModificarSensorButton);
        modificarSensorButton.setOnClickListener(v -> modificarSensor());

        Button eliminarSensorButton = findViewById(R.id.eliminarSensorButton);
        eliminarSensorButton.setOnClickListener(v -> eliminarSensor());

        Button buscarSensorButton = findViewById(R.id.buscarSensorButton);
        buscarSensorButton.setOnClickListener(v -> buscarSensor());
    }

    private void configurarSpinners() {
        cargarTiposSensor(); // Carga tipos desde Firestore
        cargarUbicaciones(); // Carga ubicaciones desde Firestore
    }


    private void actualizarSpinnerUbicaciones() {
        db.collection("ubicaciones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> ubicaciones = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ubicaciones.add(document.getString("nombre"));
                        }
                        ubicacionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ubicaciones);
                        ubicacionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerUbicacion.setAdapter(ubicacionAdapter);
                    }
                });
    }

    private void agregarSensor() {
        String nombre = nombreEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();
        String idealStr = idealEditText.getText().toString().trim();
        String ubicacionNombre = spinnerUbicacion.getSelectedItem().toString();
        String tipoNombre = spinnerTipoSensor.getSelectedItem().toString();

        if (!validarCampos(nombre, descripcion, idealStr)) return;

        float ideal = Float.parseFloat(idealStr);

        // Buscar Ubicación en Firestore
        db.collection("ubicaciones")
                .whereEqualTo("nombre", ubicacionNombre)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Ubicacion ubicacion = queryDocumentSnapshots.getDocuments().get(0).toObject(Ubicacion.class);

                        // Buscar Tipo en Firestore
                        db.collection("tipos")
                                .whereEqualTo("nombre", tipoNombre)
                                .get()
                                .addOnSuccessListener(tipoSnapshots -> {
                                    if (!tipoSnapshots.isEmpty()) {
                                        Tipo tipo = tipoSnapshots.getDocuments().get(0).toObject(Tipo.class);
                                        Sensor nuevoSensor = new Sensor(nombre, descripcion, ideal, ubicacion, tipo);

                                        // Guardar Sensor en Firestore
                                        db.collection("sensores")
                                                .add(nuevoSensor)
                                                .addOnSuccessListener(documentReference -> {
                                                    Toast.makeText(this, "Sensor agregado correctamente", Toast.LENGTH_SHORT).show();
                                                    limpiarCampos();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(this, "Error al agregar el sensor", Toast.LENGTH_SHORT).show();
                                                    e.printStackTrace(); // Depuración del error
                                                });
                                    } else {
                                        Toast.makeText(this, "Tipo no encontrado", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al buscar el tipo", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace(); // Depuración del error
                                });
                    } else {
                        Toast.makeText(this, "Ubicación no encontrada", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al buscar la ubicación", Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); // Depuración del error
                });
    }

    private void cargarUbicaciones() {
        db.collection("ubicaciones")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> ubicaciones = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        ubicaciones.add(document.getString("nombre"));
                    }
                    ubicacionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ubicaciones);
                    ubicacionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerUbicacion.setAdapter(ubicacionAdapter);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar ubicaciones", Toast.LENGTH_SHORT).show());
    }

    private void cargarTiposSensor() {
        db.collection("tipos")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> tipos = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        tipos.add(document.getString("nombre"));
                    }
                    tipoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
                    tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTipoSensor.setAdapter(tipoAdapter);
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar tipos de sensor", Toast.LENGTH_SHORT).show());
    }


    private void modificarSensor() {
        String nombre = nombreEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();
        String idealStr = idealEditText.getText().toString().trim();

        if (!validarCampos(nombre, descripcion, idealStr)) return;

        float ideal = Float.parseFloat(idealStr);

        db.collection("sensores")
                .whereEqualTo("nombre", nombre)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String sensorId = task.getResult().getDocuments().get(0).getId();
                        db.collection("sensores").document(sensorId)
                                .update("descripcion", descripcion, "ideal", ideal)
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Sensor modificado correctamente", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Error al modificar el sensor", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Sensor no encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarSensor() {
        String nombre = nombreEditText.getText().toString().trim();

        db.collection("sensores")
                .whereEqualTo("nombre", nombre)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String sensorId = task.getResult().getDocuments().get(0).getId();
                        db.collection("sensores").document(sensorId)
                                .delete()
                                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Sensor eliminado correctamente", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar el sensor", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Sensor no encontrado", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void buscarSensor() {
        String nombre = nombreEditText.getText().toString().trim();

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingrese el nombre del sensor para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ocultar el TextView antes de iniciar la búsqueda
        resultadoBusquedaTextView.setVisibility(View.GONE);

        db.collection("sensores")
                .whereEqualTo("nombre", nombre)
                .get()
                .addOnSuccessListener(task -> {
                    if (!task.isEmpty()) {
                        Sensor sensor = task.getDocuments().get(0).toObject(Sensor.class);
                        if (sensor != null) {
                            resultadoBusquedaTextView.setText(
                                    "Nombre: " + sensor.getNombre() + "\n" +
                                            "Descripción: " + sensor.getDescripcion() + "\n" +
                                            "Ideal: " + sensor.getIdeal() + "\n" +
                                            "Ubicación: " + (sensor.getUbicacion() != null ? sensor.getUbicacion().getNombre() : "No definida") + "\n" +
                                            "Tipo: " + (sensor.getTipo() != null ? sensor.getTipo().getNombre() : "No definido")
                            );
                            resultadoBusquedaTextView.setVisibility(View.VISIBLE);
                        } else {
                            resultadoBusquedaTextView.setText("Sensor no encontrado.");
                            resultadoBusquedaTextView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        resultadoBusquedaTextView.setText("Sensor no encontrado.");
                        resultadoBusquedaTextView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al buscar el sensor", Toast.LENGTH_SHORT).show();
                    resultadoBusquedaTextView.setVisibility(View.GONE);
                });
    }



    private void mostrarSensorEnUI(Sensor sensor) {
        nombreEditText.setText(sensor.getNombre());
        descripcionEditText.setText(sensor.getDescripcion());
        idealEditText.setText(String.valueOf(sensor.getIdeal()));
    }

    private boolean validarCampos(String nombre, String descripcion, String idealStr) {
        if (nombre.isEmpty() || nombre.length() < 5 || nombre.length() > 15 || nombre.contains(" ")) {
            Toast.makeText(this, "El nombre es obligatorio y debe tener entre 5 y 15 caracteres sin espacios", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (descripcion.length() > 30) {
            Toast.makeText(this, "La descripción debe tener un máximo de 30 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idealStr.isEmpty()) {
            Toast.makeText(this, "El valor ideal no puede estar vacío", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            if (Float.parseFloat(idealStr) <= 0) {
                Toast.makeText(this, "El valor ideal debe ser un número positivo", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El valor ideal debe ser un número", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        nombreEditText.setText("");
        descripcionEditText.setText("");
        idealEditText.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarSpinnerUbicaciones();
    }
}

