package com.example.trabajo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trabajo.model.Sensor;
import com.example.trabajo.repositorio.Repositorio;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private Spinner spinnerUbicacion, spinnerTipoSensor;
    private EditText nombreEditText, descripcionEditText, idealEditText;
    private ArrayAdapter<String> ubicacionAdapter, tipoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Inicialización de vistas
        spinnerTipoSensor = findViewById(R.id.SpinnerSensor);
        spinnerUbicacion = findViewById(R.id.UbiSensor);
        nombreEditText = findViewById(R.id.sensores);
        descripcionEditText = findViewById(R.id.modeloSensorEditText);
        idealEditText = findViewById(R.id.campoIdealEditText);

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
    }

    private void configurarSpinners() {
        // Configuración de spinnerTipoSensor
        List<String> tiposSensor = Repositorio.getInstance().obtenerTiposSensor();
        tipoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposSensor);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoSensor.setAdapter(tipoAdapter);

        // Configuración de spinnerUbicacion
        actualizarSpinnerUbicaciones();
    }

    private void agregarSensor() {
        String nombre = nombreEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();
        String idealStr = idealEditText.getText().toString().trim();

        if (!validarCampos(nombre, descripcion, idealStr)) return;

        // Crear y agregar el nuevo sensor al repositorio
        float ideal = Float.parseFloat(idealStr);
        Sensor nuevoSensor = new Sensor(nombre, descripcion, ideal);
        Repositorio.getInstance().agregarSensor(nuevoSensor);

        Toast.makeText(this, "Sensor agregado correctamente", Toast.LENGTH_SHORT).show();

        // Limpiar campos
        limpiarCampos();
    }

    private void actualizarSpinnerUbicaciones() {
        List<String> ubicaciones = Repositorio.getInstance().obtenerNombresUbicaciones();
        ubicacionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ubicaciones);
        ubicacionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUbicacion.setAdapter(ubicacionAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarSpinnerUbicaciones();
    }

    // Método para validar los campos de entrada
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

    // Método para limpiar los campos de entrada después de agregar un sensor
    private void limpiarCampos() {
        nombreEditText.setText("");
        descripcionEditText.setText("");
        idealEditText.setText("");
    }
}