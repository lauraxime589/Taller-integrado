package com.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventoHistorial {
    private LocalDateTime fecha;
    private String descripcion;

    public EventoHistorial(LocalDateTime fecha, String descripcion) {
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }

    public String getFechaFormato() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm a");
        return fecha.format(formatter);
    }

    @Override
    public String toString() {
        return getFechaFormato() + " - " + descripcion;
    }
}