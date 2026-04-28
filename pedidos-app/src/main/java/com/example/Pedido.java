package com.example;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Pedido {
    public enum Estado {
        SOLICITADO("Solicitado"), REGISTRADO("Registrado"), EN_PROCESO("En proceso"), DESPACHADO("Despachado");
        
        private final String label;
        Estado(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private int id;
    private Cliente cliente;
    private List<Producto> productos;
    private Estado estado;
    private LocalDateTime fechaRegistro;
    private String observaciones;
    private List<EventoHistorial> historial;
    private double total;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.productos = new ArrayList<>();
        this.estado = Estado.SOLICITADO;
        this.fechaRegistro = LocalDateTime.now();
        this.observaciones = "";
        this.historial = new ArrayList<>();
        this.total = 0.0;
        agregarEvento("Pedido solicitado");
    }

    // Getters y setters
    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<Producto> getProductos() { return productos; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { 
        this.estado = estado;
        agregarEvento("Estado: " + estado.getLabel());
    }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String obs) { this.observaciones = obs; }
    public List<EventoHistorial> getHistorial() { return historial; }
    public double getTotal() { return total; }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
        calcularTotal();
    }

    public void eliminarProducto(int index) {
        if (index >= 0 && index < productos.size()) {
            productos.remove(index);
            calcularTotal();
        }
    }

    private void calcularTotal() {
        total = productos.stream().mapToDouble(p -> p.getPrecio() * p.getCantidad()).sum();
    }

    private void agregarEvento(String descripcion) {
        historial.add(new EventoHistorial(LocalDateTime.now(), descripcion));
    }

    @Override
    public String toString() {
        return "Pedido ID: " + id + ", Cliente: " + cliente + ", Estado: " + estado + ", Total: $" + total;
    }
}