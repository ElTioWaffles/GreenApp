package com.example.trabajo;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.trabajo.repositorio.Repositorio;

public class gestor_ubi extends AppCompatActivity {
    private EditText ubiSensorEditText;
    private EditText descripcionEditText;
    private Button ingresarUbiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestor_ubi);

        // Inicialización de vistas
        ubiSensorEditText = findViewById(R.id.UbiSensor2);
        descripcionEditText = findViewById(R.id.descripc);
        ingresarUbiButton = findViewById(R.id.ingresarUbiButton);

        // Ajuste de márgenes
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configuración del listener para el botón
        ingresarUbiButton.setOnClickListener(v -> validarDatos());
    }

    private void validarDatos() {
        String nombre = ubiSensorEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();

        // Validación del nombre
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show();
            return;
        } else if (nombre.length() < 5 || nombre.length() > 15) {
            Toast.makeText(this, "El nombre debe tener entre 5 y 15 caracteres", Toast.LENGTH_SHORT).show();
            return;
        } else if (nombre.contains(" ")) {
            Toast.makeText(this, "El nombre no debe contener espacios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación de la descripción
        if (!TextUtils.isEmpty(descripcion) && descripcion.length() > 30) {
            Toast.makeText(this, "La descripción no debe exceder los 30 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Agregar la nueva ubicación al repositorio
        Repositorio.getInstance().agregarUbicacion(nombre, descripcion);
        Toast.makeText(this, "Ubicación guardada correctamente", Toast.LENGTH_SHORT).show();

        // Finalizar la actividad y regresar a SensorActivity
        finish();
    }
}
