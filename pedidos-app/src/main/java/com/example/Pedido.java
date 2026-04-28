package com.example;

import java.util.List;
import java.util.ArrayList;

public class Pedido {
    public enum Estado {
        SOLICITADO, REGISTRADO, DESPACHADO
    }

    private int id;
    private Cliente cliente;
    private List<Producto> productos;
    private Estado estado;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.productos = new ArrayList<>();
        this.estado = Estado.SOLICITADO;
    }

    // Getters y setters
    public int getId() { return id; }

    public Cliente getCliente() { return cliente; }

    public List<Producto> getProductos() { return productos; }

    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    @Override
    public String toString() {
        return "Pedido ID: " + id + ", Cliente: " + cliente + ", Estado: " + estado + ", Productos: " + productos;
    }
}