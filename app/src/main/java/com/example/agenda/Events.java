package com.example.agenda;

import java.io.Serializable;

public class Events implements Serializable {
    private int id;
    private String titulo;
    private String actividad;
    private String fecha;
    private String hora;

    public Events(int id,String titulo, String actividad, String fecha, String hora) {
        this.id = id;
        this.titulo = titulo;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Events(String titulo,String actividad, String fecha, String hora) {
        this.titulo = titulo;
        this.actividad = actividad;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
