package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class App extends Application {

    private PedidoService pedidoService = new PedidoService();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Pedidos");

        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(10));

        // Campos para registrar pedido
        TextField clienteNombre = new TextField();
        clienteNombre.setPromptText("Nombre del cliente");

        TextField clienteEmail = new TextField();
        clienteEmail.setPromptText("Email del cliente");

        TextField productoNombre = new TextField();
        productoNombre.setPromptText("Nombre del producto");

        TextField productoPrecio = new TextField();
        productoPrecio.setPromptText("Precio");

        TextField productoCantidad = new TextField();
        productoCantidad.setPromptText("Cantidad");

        Button btnRegistrar = new Button("Registrar Pedido");
        Label lblRegistro = new Label();

        // Campos para consultar
        TextField idConsulta = new TextField();
        idConsulta.setPromptText("ID del pedido");

        Button btnConsultar = new Button("Consultar Pedido");
        Label lblConsulta = new Label();

        // Campos para despachar
        TextField idDespacho = new TextField();
        idDespacho.setPromptText("ID del pedido a despachar");

        Button btnDespachar = new Button("Despachar Pedido");
        Label lblDespacho = new Label();

        // Acciones
        btnRegistrar.setOnAction(e -> {
            try {
                String nombre = clienteNombre.getText();
                String email = clienteEmail.getText();
                String prodNom = productoNombre.getText();
                double precio = Double.parseDouble(productoPrecio.getText());
                int cant = Integer.parseInt(productoCantidad.getText());

                Cliente cliente = new Cliente(nombre, email);
                Producto producto = new Producto(prodNom, precio, cant);
                List<Producto> productos = new ArrayList<>();
                productos.add(producto);

                Pedido pedido = pedidoService.registrarPedido(cliente, productos);
                lblRegistro.setText("Pedido registrado con ID: " + pedido.getId());
            } catch (Exception ex) {
                lblRegistro.setText("Error: " + ex.getMessage());
            }
        });

        btnConsultar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idConsulta.getText());
                Optional<Pedido> pedidoOpt = pedidoService.consultarPedido(id);
                if (pedidoOpt.isPresent()) {
                    lblConsulta.setText(pedidoOpt.get().toString());
                } else {
                    lblConsulta.setText("Pedido no encontrado");
                }
            } catch (Exception ex) {
                lblConsulta.setText("Error: " + ex.getMessage());
            }
        });

        btnDespachar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idDespacho.getText());
                boolean success = pedidoService.despacharPedido(id);
                if (success) {
                    lblDespacho.setText("Pedido despachado");
                } else {
                    lblDespacho.setText("No se pudo despachar");
                }
            } catch (Exception ex) {
                lblDespacho.setText("Error: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(
            new Label("Registrar Pedido"),
            clienteNombre, clienteEmail, productoNombre, productoPrecio, productoCantidad, btnRegistrar, lblRegistro,
            new Separator(),
            new Label("Consultar Pedido"),
            idConsulta, btnConsultar, lblConsulta,
            new Separator(),
            new Label("Despachar Pedido"),
            idDespacho, btnDespachar, lblDespacho
        );

        Scene scene = new Scene(root, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}