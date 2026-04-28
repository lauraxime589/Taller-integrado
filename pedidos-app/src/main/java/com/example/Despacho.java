package com.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Despacho {
    private int idPedido;
    private LocalDate fechaDespacho;
    private String transportista;
    private String numeroGuia;
    private String observaciones;

    public Despacho(int idPedido, LocalDate fechaDespacho, String transportista, String numeroGuia) {
        this.idPedido = idPedido;
        this.fechaDespacho = fechaDespacho;
        this.transportista = transportista;
        this.numeroGuia = numeroGuia;
        this.observaciones = "";
    }

    // Getters y setters
    public int getIdPedido() { return idPedido; }
    public LocalDate getFechaDespacho() { return fechaDespacho; }
    public void setFechaDespacho(LocalDate fecha) { this.fechaDespacho = fecha; }
    
    public String getTransportista() { return transportista; }
    public void setTransportista(String transportista) { this.transportista = transportista; }
    
    public String getNumeroGuia() { return numeroGuia; }
    public void setNumeroGuia(String numeroGuia) { this.numeroGuia = numeroGuia; }
    
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String obs) { this.observaciones = obs; }

    public String getFechaFormato() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fechaDespacho.format(formatter);
    }

    @Override
    public String toString() {
        return "Despacho - Pedido: " + idPedido + ", Transportista: " + transportista + 
               ", Guía: " + numeroGuia + ", Fecha: " + getFechaFormato();
    }
}