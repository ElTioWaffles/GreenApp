package com.example.trabajo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    private ArrayAdapter<String> ubicacionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Referencia a los Spinners y campos de texto
        spinnerTipoSensor = findViewById(R.id.SpinnerSensor);
        spinnerUbicacion = findViewById(R.id.UbiSensor);
        nombreEditText = findViewById(R.id.sensores);
        descripcionEditText = findViewById(R.id.modeloSensorEditText);
        idealEditText = findViewById(R.id.campoIdealEditText);

        // Obtener datos del repositorio
        List<String> tiposSensor = Repositorio.getInstance().obtenerTiposSensor();
        List<String> ubicaciones = Repositorio.getInstance().obtenerUbicaciones();

        // Configurar adaptadores para los Spinners
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tiposSensor);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoSensor.setAdapter(tipoAdapter);

        actualizarSpinnerUbicaciones();

        // Configuración del botón para abrir la lista de sensores
        Button verSensoresButton = findViewById(R.id.verSensoresButton);
        verSensoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SensorActivity.this, recyclerSensor.class);
                startActivity(intent);
            }
        });

        // Configuración del botón "Ingresar Sensor" para validar y procesar los datos
        Button ingresarSensorButton = findViewById(R.id.ingresarSensorButton);
        ingresarSensorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarSensor();
            }
        });
    }

    // Método para agregar un sensor al repositorio
    private void agregarSensor() {
        String nombre = nombreEditText.getText().toString();
        String descripcion = descripcionEditText.getText().toString();
        String idealStr = idealEditText.getText().toString();

        if (validarCampoIdeal(idealStr) && validarNombre(nombre) && validarDescripcion(descripcion)) {
            float ideal = Float.parseFloat(idealStr);

            Sensor nuevoSensor = new Sensor(nombre, descripcion, ideal);
            Repositorio.getInstance().agregarSensor(nuevoSensor);

            Toast.makeText(SensorActivity.this, "Sensor agregado correctamente", Toast.LENGTH_SHORT).show();

            // Limpiar los campos después de agregar el sensor
            nombreEditText.setText("");
            descripcionEditText.setText("");
            idealEditText.setText("");
        }
    }

    // Método para actualizar el Spinner de ubicaciones
    private void actualizarSpinnerUbicaciones() {
        List<String> ubicaciones = Repositorio.getInstance().obtenerUbicaciones();
        ubicacionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ubicaciones);
        ubicacionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUbicacion.setAdapter(ubicacionAdapter);
    }

    // Sobrescribir onResume para actualizar las ubicaciones cuando se regrese a SensorActivity
    @Override
    protected void onResume() {
        super.onResume();
        actualizarSpinnerUbicaciones();
    }

    // Método para validar que el campo ideal sea un valor positivo
    private boolean validarCampoIdeal(String campoIdeal) {
        try {
            float valor = Float.parseFloat(campoIdeal);
            if (valor > 0) {
                return true;
            } else {
                Toast.makeText(this, "El valor ideal debe ser un número positivo", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El valor ideal debe ser un número", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Método para validar que el nombre es obligatorio y tiene entre 5 y 15 caracteres
    private boolean validarNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return false;
        } else if (nombre.length() < 5 || nombre.length() > 15) {
            Toast.makeText(this, "El nombre debe tener entre 5 y 15 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Método para validar que la descripción es opcional pero no debe exceder los 30 caracteres
    private boolean validarDescripcion(String descripcion) {
        if (descripcion != null && descripcion.length() > 30) {
            Toast.makeText(this, "La descripción debe tener un máximo de 30 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}


