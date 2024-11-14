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
import com.example.trabajo.repositorio.Repositorio;

import java.util.List;

public class gestor_ubi extends AppCompatActivity {
    private EditText ubiSensorEditText;
    private EditText descripcionEditText;
    private Button ingresarUbiButton;
    private RecyclerView recyclerUbicaciones;
    private UbicacionAdapter ubicacionAdapter;
    private List<Ubicacion> ubicaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor_ubi);

        // Inicialización de vistas
        ubiSensorEditText = findViewById(R.id.UbiSensor2);
        descripcionEditText = findViewById(R.id.descripc);
        ingresarUbiButton = findViewById(R.id.ingresarUbiButton);
        recyclerUbicaciones = findViewById(R.id.recyclerUbicaciones);

        // Configuración inicial del RecyclerView
        ubicaciones = Repositorio.getInstance().obtenerListaUbicaciones();
        ubicacionAdapter = new UbicacionAdapter(ubicaciones);
        recyclerUbicaciones.setLayoutManager(new LinearLayoutManager(this));
        recyclerUbicaciones.setAdapter(ubicacionAdapter);

        // Configuración del botón para agregar ubicaciones
        ingresarUbiButton.setOnClickListener(v -> agregarUbicacion());
    }

    private void agregarUbicacion() {
        String nombre = ubiSensorEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();

        // Verificar validación de campos
        if (!validarCampos(nombre, descripcion)) return;

        // Agregar ubicación al repositorio y actualizar la vista
        Repositorio.getInstance().agregarUbicacion(nombre, descripcion);
        ubicacionAdapter.notifyItemInserted(ubicaciones.size() - 1);

        Toast.makeText(this, "Ubicación guardada correctamente", Toast.LENGTH_SHORT).show();

        // Limpiar campos de entrada
        ubiSensorEditText.setText("");
        descripcionEditText.setText("");
    }

    // Método para validar campos y mostrar mensajes de error si es necesario
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
}