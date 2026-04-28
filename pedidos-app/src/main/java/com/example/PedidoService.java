package com.example;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class PedidoService {
    private List<Pedido> pedidos = new ArrayList<>();
    private int nextId = 1;

    public Pedido registrarPedido(Cliente cliente, List<Producto> productos) {
        Pedido pedido = new Pedido(nextId++, cliente);
        for (Producto p : productos) {
            pedido.agregarProducto(p);
        }
        pedido.setEstado(Pedido.Estado.REGISTRADO);
        pedidos.add(pedido);
        return pedido;
    }

    public Optional<Pedido> consultarPedido(int id) {
        return pedidos.stream().filter(p -> p.getId() == id).findFirst();
    }

    public boolean despacharPedido(int id) {
        Optional<Pedido> pedidoOpt = consultarPedido(id);
        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            if (pedido.getEstado() == Pedido.Estado.REGISTRADO) {
                pedido.setEstado(Pedido.Estado.DESPACHADO);
                return true;
            }
        }
        return false;
    }

    public List<Pedido> getAllPedidos() {
        return new ArrayList<>(pedidos);
    }
}