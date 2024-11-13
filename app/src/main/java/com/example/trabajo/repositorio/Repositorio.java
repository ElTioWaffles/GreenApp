package com.example.trabajo.repositorio;

import com.example.trabajo.model.Tipo;
import com.example.trabajo.model.Ubicacion;
import com.example.trabajo.model.Sensor;

import java.util.ArrayList;
import java.util.List;

public class Repositorio {

    private static Repositorio instance = null;
    private List<Tipo> listaTipoSensor;
    private List<Ubicacion> listaUbicaciones;
    private List<Sensor> listaSensores;

    protected Repositorio() {
        listaTipoSensor = new ArrayList<>();
        listaUbicaciones = new ArrayList<>();
        listaSensores = new ArrayList<>();

        // Datos de muestra para tipo de sensor
        listaTipoSensor.add(new Tipo("Temperatura"));
        listaTipoSensor.add(new Tipo("Humedad"));

        // Datos de muestra para ubicaciones
        listaUbicaciones.add(new Ubicacion("Invernadero Norte", "Ubicación principal para cultivo de plantas tropicales."));
        listaUbicaciones.add(new Ubicacion("Invernadero Sur", "Invernadero con temperatura controlada para cultivos más delicados."));
        listaUbicaciones.add(new Ubicacion("Invernadero Este", "Área utilizada para cultivos de hortalizas y plantas de bajo mantenimiento."));

        // Datos de muestra para sensores
        listaSensores.add(new Sensor("Sensor de Temperatura", "Temperatura", 0.0f));
        listaSensores.add(new Sensor("Sensor de Humedad", "Humedad", 0.0f));
    }

    // Singleton para obtener la instancia única de Repositorio
    public static synchronized Repositorio getInstance() {
        if (instance == null) {
            instance = new Repositorio();
        }
        return instance;
    }

    // Método para obtener los tipos de sensor en una lista de String para el Spinner
    public List<String> obtenerTiposSensor() {
        List<String> tipos = new ArrayList<>();
        for (Tipo tipo : listaTipoSensor) {
            tipos.add(tipo.getNombre());
        }
        return tipos;
    }

    // Método para obtener los nombres de ubicaciones en una lista de String para el Spinner
    public List<String> obtenerNombresUbicaciones() {
        List<String> nombresUbicaciones = new ArrayList<>();
        for (Ubicacion ubicacion : listaUbicaciones) {
            nombresUbicaciones.add(ubicacion.getNombre());
        }
        return nombresUbicaciones;
    }

    // Método para obtener la lista completa de ubicaciones como List<Ubicacion>
    public List<Ubicacion> obtenerListaUbicaciones() {
        return listaUbicaciones;
    }

    // Método para agregar una nueva ubicación si no existe ya en la lista
    public void agregarUbicacion(String nombre, String descripcion) {
        for (Ubicacion ubicacion : listaUbicaciones) {
            if (ubicacion.getNombre().equalsIgnoreCase(nombre)) {
                return; // No agregar si ya existe
            }
        }
        listaUbicaciones.add(new Ubicacion(nombre, descripcion));
    }

    // Método para agregar un sensor a la lista de sensores
    public void agregarSensor(Sensor sensor) {
        listaSensores.add(sensor);
    }

    // Método para obtener la lista de sensores
    public List<Sensor> obtenerSensores() {
        return listaSensores;
    }
}

