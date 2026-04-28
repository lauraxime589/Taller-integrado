package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.time.LocalDate;

public class PedidoService {
    private List<Pedido> pedidos = new ArrayList<>();
    private List<Despacho> despachos = new ArrayList<>();
    private int nextId = 1;

    public Pedido registrarPedido(Cliente cliente, List<Producto> productos, String observaciones) {
        Pedido pedido = new Pedido(nextId++, cliente);
        for (Producto p : productos) {
            pedido.agregarProducto(p);
        }
        if (!observaciones.isEmpty()) {
            pedido.setObservaciones(observaciones);
        }
        pedido.setEstado(Pedido.Estado.REGISTRADO);
        pedidos.add(pedido);
        return pedido;
    }

    public Optional<Pedido> consultarPedido(int id) {
        return pedidos.stream().filter(p -> p.getId() == id).findFirst();
    }

    public Despacho registrarDespacho(int idPedido, LocalDate fechaDespacho, String transportista, 
                                      String numeroGuia, String observaciones) {
        Optional<Pedido> pedidoOpt = consultarPedido(idPedido);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            if (pedido.getEstado() == Pedido.Estado.REGISTRADO) {
                Despacho despacho = new Despacho(idPedido, fechaDespacho, transportista, numeroGuia);
                despacho.setObservaciones(observaciones);
                pedido.setEstado(Pedido.Estado.DESPACHADO);
                despachos.add(despacho);
                return despacho;
            }
        }
        return null;
    }

    public Optional<Despacho> consultarDespacho(int idPedido) {
        return despachos.stream().filter(d -> d.getIdPedido() == idPedido).findFirst();
    }

    public List<Pedido> getAllPedidos() {
        return new ArrayList<>(pedidos);
    }

    public List<Despacho> getAllDespachos() {
        return new ArrayList<>(despachos);
    }
}