package com.example.trabajo.model;

public class Sensor {
    private String nombre;
    private String descripcion;
    private float ideal;


    public Sensor(String nombre, String descripcion, float ideal) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ideal = ideal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getIdeal() {
        return ideal;
    }

    public void setIdeal(float ideal) {
        this.ideal = ideal;
    }


}
