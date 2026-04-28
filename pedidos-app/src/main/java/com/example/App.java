package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class App extends Application {

    private PedidoService pedidoService = new PedidoService();
    private List<Producto> productosDisponibles = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Pedido y Despacho");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);

        // Inicializar productos disponibles
        inicializarProductos();

        // Crear TabPane con las tres pestañas
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tab1 = new Tab("1. Registrar Pedido", crearPantallaRegistroPedido());
        Tab tab2 = new Tab("2. Consultar Pedido", crearPantallaConsultaPedido());
        Tab tab3 = new Tab("3. Registrar Despacho", crearPantallaRegistroDespacho());

        tabPane.getTabs().addAll(tab1, tab2, tab3);

        Scene scene = new Scene(tabPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void inicializarProductos() {
        productosDisponibles.add(new Producto("Producto A", 10.00, 1));
        productosDisponibles.add(new Producto("Producto B", 15.00, 1));
        productosDisponibles.add(new Producto("Producto C", 20.00, 1));
        productosDisponibles.add(new Producto("Producto D", 25.00, 1));
    }

    // ==================== PANTALLA 1: REGISTRAR PEDIDO ====================
    private VBox crearPantallaRegistroPedido() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        // Título
        Label titulo = new Label("Registrar Pedido");
        titulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Sección Datos del Cliente
        VBox datosCliente = new VBox(10);
        datosCliente.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");

        Label lblDatosCliente = new Label("Datos del cliente");
        lblDatosCliente.setStyle("-fx-font-weight: bold;");

        HBox hboxCliente = new HBox(20);
        TextField txClienteNombre = new TextField();
        txClienteNombre.setPromptText("Nombre del cliente");
        txClienteNombre.setPrefWidth(200);

        TextField txClienteContacto = new TextField();
        txClienteContacto.setPromptText("Teléfono / Email");
        txClienteContacto.setPrefWidth(200);

        hboxCliente.getChildren().addAll(new Label("Cliente:"), txClienteNombre, new Label("Contacto:"), txClienteContacto);
        datosCliente.getChildren().addAll(lblDatosCliente, hboxCliente);

        // Sección Agregar Productos
        VBox agregarProductos = new VBox(10);
        agregarProductos.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");

        Label lblAgregarProductos = new Label("Agregar productos");
        lblAgregarProductos.setStyle("-fx-font-weight: bold;");

        HBox hboxProducto = new HBox(10);
        ComboBox<String> cbProducto = new ComboBox<>();
        cbProducto.setItems(FXCollections.observableArrayList(
            productosDisponibles.stream().map(Producto::getNombre).toArray(String[]::new)
        ));
        cbProducto.setPromptText("-- Seleccionar producto --");
        cbProducto.setPrefWidth(200);

        Spinner<Integer> spinnerCantidad = new Spinner<>(1, 100, 1);
        spinnerCantidad.setPrefWidth(100);

        Button btnAgregarProducto = new Button("Agregar producto");

        hboxProducto.getChildren().addAll(cbProducto, new Label("Cantidad:"), spinnerCantidad, btnAgregarProducto);
        agregarProductos.getChildren().addAll(lblAgregarProductos, hboxProducto);

        // Tabla de productos agregados
        TableView<Producto> tablaProductos = new TableView<>();
        tablaProductos.setPrefHeight(200);

        TableColumn<Producto, String> colNombre = new TableColumn<>("Producto");
        colNombre.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getNombre()));

        TableColumn<Producto, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(p.getValue().getCantidad()));

        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio Unit.");
        colPrecio.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(p.getValue().getPrecio()));

        TableColumn<Producto, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(
            p.getValue().getPrecio() * p.getValue().getCantidad()
        ));

        TableColumn<Producto, Void> colAccion = new TableColumn<>("Acción");
        colAccion.setCellFactory(pc -> new TableCell<Producto, Void>() {
            private final Button btn = new Button("🗑");
            {
                btn.setStyle("-fx-font-size: 12; -fx-padding: 5;");
                btn.setOnAction(event -> tablaProductos.getItems().remove(getIndex()));
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        tablaProductos.getColumns().addAll(colNombre, colCantidad, colPrecio, colSubtotal, colAccion);

        // Label total
        Label lblTotal = new Label("Total: $0.00");
        lblTotal.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        // Observaciones
        Label lblObs = new Label("Observaciones:");
        lblObs.setStyle("-fx-font-weight: bold;");
        TextArea txObservaciones = new TextArea();
        txObservaciones.setWrapText(true);
        txObservaciones.setPrefRowCount(4);

        // Botones
        HBox botones = new HBox(10);
        botones.setStyle("-fx-alignment: center-left;");
        Button btnGuardar = new Button("Guardar Pedido");
        btnGuardar.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-font-size: 12; -fx-padding: 10;");

        botones.getChildren().addAll(btnGuardar, btnCancelar);

        // Acciones
        btnAgregarProducto.setOnAction(e -> {
            if (cbProducto.getValue() != null) {
                String nombre = cbProducto.getValue();
                Producto prod = productosDisponibles.stream()
                    .filter(p -> p.getNombre().equals(nombre))
                    .findFirst()
                    .orElse(null);
                if (prod != null) {
                    Producto copia = new Producto(prod.getNombre(), prod.getPrecio(), spinnerCantidad.getValue());
                    tablaProductos.getItems().add(copia);
                    cbProducto.setValue(null);
                    spinnerCantidad.getValueFactory().setValue(1);
                    actualizarTotal(tablaProductos, lblTotal);
                }
            }
        });

        tablaProductos.getItems().addListener((javafx.collections.ListChangeListener<Producto>) c -> 
            actualizarTotal(tablaProductos, lblTotal)
        );

        btnGuardar.setOnAction(e -> {
            if (txClienteNombre.getText().isEmpty() || tablaProductos.getItems().isEmpty()) {
                mostrarAlerta("Error", "Debe completar cliente y productos");
            } else {
                Cliente cliente = new Cliente(txClienteNombre.getText(), txClienteContacto.getText());
                Pedido pedido = pedidoService.registrarPedido(cliente, new ArrayList<>(tablaProductos.getItems()), txObservaciones.getText());
                mostrarAlerta("Éxito", "Pedido registrado con ID: " + pedido.getId());
                txClienteNombre.clear();
                txClienteContacto.clear();
                txObservaciones.clear();
                tablaProductos.getItems().clear();
                lblTotal.setText("Total: $0.00");
            }
        });

        btnCancelar.setOnAction(e -> {
            txClienteNombre.clear();
            txClienteContacto.clear();
            txObservaciones.clear();
            tablaProductos.getItems().clear();
            lblTotal.setText("Total: $0.00");
        });

        root.getChildren().addAll(
            titulo, datosCliente, agregarProductos, hboxProducto,
            new Label("Productos agregados:"), tablaProductos, lblTotal,
            lblObs, txObservaciones, botones
        );

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        return new VBox(scroll);
    }

    // ==================== PANTALLA 2: CONSULTAR PEDIDO ====================
    private VBox crearPantallaConsultaPedido() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titulo = new Label("Consultar Pedido");
        titulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Búsqueda
        HBox busqueda = new HBox(10);
        busqueda.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");
        Label lblBuscar = new Label("Buscar pedido");
        lblBuscar.setStyle("-fx-font-weight: bold;");

        TextField txIdBuscar = new TextField();
        txIdBuscar.setPromptText("Ingrese número de pedido");
        txIdBuscar.setPrefWidth(200);

        Button btnBuscar = new Button("Buscar");

        busqueda.getChildren().addAll(lblBuscar, txIdBuscar, btnBuscar);

        // Información del pedido
        VBox infoPedido = new VBox(10);
        infoPedido.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");

        Label lblInfoPedido = new Label("Información del pedido");
        lblInfoPedido.setStyle("-fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

        Label lbl1 = new Label("N° Pedido:");
        TextField tx1 = new TextField();
        tx1.setEditable(false);

        Label lbl2 = new Label("Cliente:");
        TextField tx2 = new TextField();
        tx2.setEditable(false);

        Label lbl3 = new Label("Contacto:");
        TextField tx3 = new TextField();
        tx3.setEditable(false);

        Label lbl4 = new Label("Fecha pedido:");
        TextField tx4 = new TextField();
        tx4.setEditable(false);

        Label lbl5 = new Label("Estado:");
        TextField tx5 = new TextField();
        tx5.setEditable(false);
        tx5.setStyle("-fx-text-fill: #0066cc;");

        Label lbl6 = new Label("Total:");
        TextField tx6 = new TextField();
        tx6.setEditable(false);

        Label lbl7 = new Label("Observaciones:");
        TextArea tx7 = new TextArea();
        tx7.setEditable(false);
        tx7.setPrefRowCount(3);
        tx7.setWrapText(true);

        grid.add(lbl1, 0, 0);
        grid.add(tx1, 1, 0);
        grid.add(lbl2, 0, 1);
        grid.add(tx2, 1, 1);
        grid.add(lbl3, 0, 2);
        grid.add(tx3, 1, 2);
        grid.add(lbl4, 0, 3);
        grid.add(tx4, 1, 3);
        grid.add(lbl5, 2, 0);
        grid.add(tx5, 3, 0);
        grid.add(lbl6, 2, 1);
        grid.add(tx6, 3, 1);
        grid.add(lbl7, 0, 4);
        grid.add(tx7, 1, 4, 3, 2);

        infoPedido.getChildren().addAll(lblInfoPedido, grid);

        // Tabla de productos
        Label lblProductos = new Label("Detalle de productos");
        lblProductos.setStyle("-fx-font-weight: bold;");

        TableView<Producto> tablaProductos = new TableView<>();
        tablaProductos.setPrefHeight(150);

        TableColumn<Producto, String> colNombre = new TableColumn<>("Producto");
        colNombre.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getNombre()));

        TableColumn<Producto, Integer> colCantidad = new TableColumn<>("Cantidad");
        colCantidad.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(p.getValue().getCantidad()));

        TableColumn<Producto, Double> colPrecio = new TableColumn<>("Precio Unit.");
        colPrecio.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(p.getValue().getPrecio()));

        TableColumn<Producto, Double> colSubtotal = new TableColumn<>("Subtotal");
        colSubtotal.setCellValueFactory(p -> new javafx.beans.property.SimpleObjectProperty<>(
            p.getValue().getPrecio() * p.getValue().getCantidad()
        ));

        tablaProductos.getColumns().addAll(colNombre, colCantidad, colPrecio, colSubtotal);

        // Historial
        Label lblHistorial = new Label("Historial del pedido");
        lblHistorial.setStyle("-fx-font-weight: bold;");

        ListView<EventoHistorial> listaHistorial = new ListView<>();
        listaHistorial.setPrefHeight(150);

        // Botón volver
        Button btnVolver = new Button("Volver");
        btnVolver.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        btnVolver.setOnAction(e -> {
            tx1.clear();
            tx2.clear();
            tx3.clear();
            tx4.clear();
            tx5.clear();
            tx6.clear();
            tx7.clear();
            tablaProductos.getItems().clear();
            listaHistorial.getItems().clear();
            txIdBuscar.clear();
        });

        btnBuscar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txIdBuscar.getText());
                Optional<Pedido> pedidoOpt = pedidoService.consultarPedido(id);
                if (pedidoOpt.isPresent()) {
                    Pedido pedido = pedidoOpt.get();
                    tx1.setText(String.valueOf(pedido.getId()));
                    tx2.setText(pedido.getCliente().getNombre());
                    tx3.setText(pedido.getCliente().getEmail());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    tx4.setText(pedido.getFechaRegistro().format(formatter));
                    tx5.setText(pedido.getEstado().getLabel());
                    tx6.setText("$" + String.format("%.2f", pedido.getTotal()));
                    tx7.setText(pedido.getObservaciones());
                    tablaProductos.setItems(FXCollections.observableArrayList(pedido.getProductos()));
                    listaHistorial.setItems(FXCollections.observableArrayList(pedido.getHistorial()));
                } else {
                    mostrarAlerta("Error", "Pedido no encontrado");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Ingrese un ID válido");
            }
        });

        root.getChildren().addAll(
            titulo, busqueda, infoPedido, lblProductos, tablaProductos,
            lblHistorial, listaHistorial, btnVolver
        );

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        return new VBox(scroll);
    }

    // ==================== PANTALLA 3: REGISTRAR DESPACHO ====================
    private VBox crearPantallaRegistroDespacho() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label titulo = new Label("Registrar Despacho");
        titulo.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        // Búsqueda
        HBox busqueda = new HBox(10);
        busqueda.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");
        Label lblBuscar = new Label("Buscar pedido");
        lblBuscar.setStyle("-fx-font-weight: bold;");

        TextField txIdDespacho = new TextField();
        txIdDespacho.setPromptText("Ingrese número de pedido");
        txIdDespacho.setPrefWidth(200);

        Button btnBuscar = new Button("Buscar");

        busqueda.getChildren().addAll(lblBuscar, txIdDespacho, btnBuscar);

        // Información del pedido
        VBox infoPedido = new VBox(10);
        infoPedido.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");

        Label lblInfoPedido = new Label("Información del pedido");
        lblInfoPedido.setStyle("-fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(10);

        TextField txNPedido = new TextField();
        txNPedido.setEditable(false);
        TextField txCliente = new TextField();
        txCliente.setEditable(false);
        TextField txContacto = new TextField();
        txContacto.setEditable(false);
        TextField txFechaPedido = new TextField();
        txFechaPedido.setEditable(false);
        TextField txEstado = new TextField();
        txEstado.setEditable(false);
        txEstado.setStyle("-fx-text-fill: #0066cc;");
        TextField txTotal = new TextField();
        txTotal.setEditable(false);

        grid.add(new Label("N° Pedido:"), 0, 0);
        grid.add(txNPedido, 1, 0);
        grid.add(new Label("Cliente:"), 0, 1);
        grid.add(txCliente, 1, 1);
        grid.add(new Label("Contacto:"), 0, 2);
        grid.add(txContacto, 1, 2);
        grid.add(new Label("Fecha pedido:"), 2, 0);
        grid.add(txFechaPedido, 3, 0);
        grid.add(new Label("Estado actual:"), 2, 1);
        grid.add(txEstado, 3, 1);
        grid.add(new Label("Total:"), 2, 2);
        grid.add(txTotal, 3, 2);

        infoPedido.getChildren().addAll(lblInfoPedido, grid);

        // Datos del despacho
        VBox datosDespacho = new VBox(10);
        datosDespacho.setStyle("-fx-border-color: #ddd; -fx-border-radius: 5; -fx-padding: 10;");

        Label lblDatos = new Label("Datos del despacho");
        lblDatos.setStyle("-fx-font-weight: bold;");

        GridPane gridDespacho = new GridPane();
        gridDespacho.setHgap(20);
        gridDespacho.setVgap(10);

        DatePicker dpFechaDespacho = new DatePicker(LocalDate.now());
        ComboBox<String> cbTransportista = new ComboBox<>();
        cbTransportista.setItems(FXCollections.observableArrayList("Transportista A", "Transportista B", "Transportista C"));
        cbTransportista.setPromptText("-- Seleccionar transportista --");
        cbTransportista.setPrefWidth(200);

        TextField txGuia = new TextField();
        txGuia.setPromptText("Ingrese número de guía");
        txGuia.setPrefWidth(200);

        TextArea txObsDespacho = new TextArea();
        txObsDespacho.setPromptText("Escriba alguna observación (opcional)...");
        txObsDespacho.setWrapText(true);
        txObsDespacho.setPrefRowCount(3);

        gridDespacho.add(new Label("Fecha despacho:"), 0, 0);
        gridDespacho.add(dpFechaDespacho, 1, 0);
        gridDespacho.add(new Label("Transportista:"), 0, 1);
        gridDespacho.add(cbTransportista, 1, 1);
        gridDespacho.add(new Label("Guía / Nº Envío:"), 0, 2);
        gridDespacho.add(txGuia, 1, 2);
        gridDespacho.add(new Label("Observaciones:"), 0, 3);
        gridDespacho.add(txObsDespacho, 1, 3);

        datosDespacho.getChildren().addAll(lblDatos, gridDespacho);

        // Botones
        HBox botones = new HBox(10);
        botones.setStyle("-fx-alignment: center-left;");
        Button btnRegistrar = new Button("Registrar Despacho");
        btnRegistrar.setStyle("-fx-font-size: 12; -fx-padding: 10;");
        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-font-size: 12; -fx-padding: 10;");

        botones.getChildren().addAll(btnRegistrar, btnCancelar);

        // Acciones
        btnBuscar.setOnAction(e -> {
            try {
                int id = Integer.parseInt(txIdDespacho.getText());
                Optional<Pedido> pedidoOpt = pedidoService.consultarPedido(id);
                if (pedidoOpt.isPresent()) {
                    Pedido pedido = pedidoOpt.get();
                    if (pedido.getEstado() == Pedido.Estado.REGISTRADO) {
                        txNPedido.setText(String.valueOf(pedido.getId()));
                        txCliente.setText(pedido.getCliente().getNombre());
                        txContacto.setText(pedido.getCliente().getEmail());
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        txFechaPedido.setText(pedido.getFechaRegistro().format(formatter));
                        txEstado.setText(pedido.getEstado().getLabel());
                        txTotal.setText("$" + String.format("%.2f", pedido.getTotal()));
                    } else {
                        mostrarAlerta("Error", "El pedido no está en estado REGISTRADO");
                        limpiarDespacho(txNPedido, txCliente, txContacto, txFechaPedido, txEstado, txTotal, 
                                       dpFechaDespacho, cbTransportista, txGuia, txObsDespacho);
                    }
                } else {
                    mostrarAlerta("Error", "Pedido no encontrado");
                }
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Ingrese un ID válido");
            }
        });

        btnRegistrar.setOnAction(e -> {
            if (txNPedido.getText().isEmpty() || cbTransportista.getValue() == null || txGuia.getText().isEmpty()) {
                mostrarAlerta("Error", "Complete todos los campos requeridos");
            } else {
                try {
                    int idPedido = Integer.parseInt(txNPedido.getText());
                    Despacho despacho = pedidoService.registrarDespacho(
                        idPedido,
                        dpFechaDespacho.getValue(),
                        cbTransportista.getValue(),
                        txGuia.getText(),
                        txObsDespacho.getText()
                    );
                    if (despacho != null) {
                        mostrarAlerta("Éxito", "Despacho registrado correctamente");
                        limpiarDespacho(txNPedido, txCliente, txContacto, txFechaPedido, txEstado, txTotal,
                                       dpFechaDespacho, cbTransportista, txGuia, txObsDespacho);
                        txIdDespacho.clear();
                    } else {
                        mostrarAlerta("Error", "No se pudo registrar el despacho");
                    }
                } catch (Exception ex) {
                    mostrarAlerta("Error", ex.getMessage());
                }
            }
        });

        btnCancelar.setOnAction(e -> {
            limpiarDespacho(txNPedido, txCliente, txContacto, txFechaPedido, txEstado, txTotal,
                           dpFechaDespacho, cbTransportista, txGuia, txObsDespacho);
            txIdDespacho.clear();
        });

        root.getChildren().addAll(
            titulo, busqueda, infoPedido, datosDespacho, botones
        );

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        return new VBox(scroll);
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private void actualizarTotal(TableView<Producto> tabla, Label lblTotal) {
        double total = tabla.getItems().stream()
            .mapToDouble(p -> p.getPrecio() * p.getCantidad())
            .sum();
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarDespacho(TextField tx1, TextField tx2, TextField tx3, TextField tx4, 
                                  TextField tx5, TextField tx6, DatePicker dp, 
                                  ComboBox<String> cb, TextField tx7, TextArea ta) {
        tx1.clear();
        tx2.clear();
        tx3.clear();
        tx4.clear();
        tx5.clear();
        tx6.clear();
        tx7.clear();
        ta.clear();
        dp.setValue(LocalDate.now());
        cb.setValue(null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}