package org.mdv.view.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import org.mdv.dto.VentaDetalleResponse;
import org.mdv.dto.VentaRequest;
import org.mdv.dto.VentaResponse;
import org.mdv.model.Venta;
import org.mdv.service.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class VentaListadoController extends WindowControllerBase {

    // ----------- BUSQUEDA -----------
    @FXML
    private TextField txtBuscarCliente;
    @FXML
    private DatePicker dateBuscarFecha;
    @FXML
    private Label lblTotalDia;

    // ----------- TABLA DE VENTAS -----------
    @FXML
    private TableView<VentaResponse> tablaVentas;
    @FXML
    private TableColumn<VentaResponse, String> colFecha;
    @FXML
    private TableColumn<VentaResponse, String> colCliente;
    @FXML
    private TableColumn<VentaResponse, String> colTotal;

    // ----------- DETALLES DE LA VENTA -----------
    @FXML
    private TableView<VentaDetalleResponse> tablaDetalles;
    @FXML
    private TableColumn<VentaDetalleResponse, String> colProd;
    @FXML
    private TableColumn<VentaDetalleResponse, String> colCant;
    @FXML
    private TableColumn<VentaDetalleResponse, String> colPrecio;
    @FXML
    private TableColumn<VentaDetalleResponse, String> colLinea;

    @FXML
    private Label lblDetalleCliente, lblDetalleFecha, lblDetalleTotal, lblSinDetalles;

    // ----------- SERVICIOS -----------
    private final VentaService ventaService;

    private final ObservableList<VentaResponse> listaVentas = FXCollections.observableArrayList();
    private final ObservableList<VentaDetalleResponse> listaDetalles = FXCollections.observableArrayList();

    public VentaListadoController() {

        ClienteService clienteService = new ClienteService();
        CategoriaService categoriaService = new CategoriaService();
        ProductoService productoService = new ProductoService(categoriaService);

        VentaService ventaTemp = new VentaService(clienteService);
        VentaDetalleService ventaDetalleService = new VentaDetalleService(ventaTemp, productoService);

        this.ventaService = new VentaService(clienteService);
    }

    @FXML
    public void initialize() {
        super.initialize();

        // ----- CONFIG TABLA -----
        colFecha.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().fechaVenta().toString()));
        colCliente.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().cliente().nombre()));
        colTotal.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().importeTotal().toPlainString()));

        colProd.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().producto().descripcion()));
        colCant.setCellValueFactory(d -> new ReadOnlyStringWrapper(String.valueOf(d.getValue().cantidad())));
        colPrecio.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().precioUnitario().toPlainString()));
        colLinea.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().totalLinea().toPlainString()));

        cargarVentas();

        // Mostrar detalles al seleccionar venta
        tablaVentas.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldV, nuevaVenta) -> mostrarDetalles(nuevaVenta));

        // Habilitar botones solo si hay venta seleccionada
        tablaVentas.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {
                    boolean seleccionado = newSel != null;
                });
    }

    // =========================
    // CARGAR LISTADO DE VENTAS
    // =========================
    private void cargarVentas() {
        listaVentas.setAll(ventaService.buscarTodos());
        tablaVentas.setItems(listaVentas);
        lblTotalDia.setText("");
    }

    @FXML
    private void handleActualizar(ActionEvent e) {
        cargarVentas();
    }

    // =========================
    // BUSCAR POR CLIENTE
    // =========================
    @FXML
    private void buscarPorCliente() {
        String texto = txtBuscarCliente.getText().trim().toLowerCase();

        if (texto.isEmpty()) {
            cargarVentas();
            return;
        }

        List<VentaResponse> filtrado = listaVentas.stream()
                .filter(v -> v.cliente().nombre().toLowerCase().contains(texto))
                .collect(Collectors.toList());

        tablaVentas.setItems(FXCollections.observableArrayList(filtrado));
    }

    // =========================
    // BUSCAR POR FECHA
    // =========================
    @FXML
    private void buscarPorFecha() {
        LocalDate fecha = dateBuscarFecha.getValue();

        if (fecha == null) {
            cargarVentas();
            return;
        }

        List<VentaResponse> filtrado = listaVentas.stream()
                .filter(v -> v.fechaVenta().equals(fecha))
                .collect(Collectors.toList());

        tablaVentas.setItems(FXCollections.observableArrayList(filtrado));
    }

    // =========================
    // TOTAL DEL DÍA
    // =========================
    @FXML
    private void calcularTotalDia() {
        LocalDate fecha = dateBuscarFecha.getValue();

        if (fecha == null) {
            lblTotalDia.setText("Seleccione una fecha.");
            return;
        }

        var total = ventaService.sumTotalDia(fecha);

        lblTotalDia.setText(total.isPresent()
                ? "Total del día: €" + total.get()
                : "Sin ventas ese día.");
    }

    // =========================
    // DETALLES DE UNA VENTA
    // =========================
    private void mostrarDetalles(VentaResponse venta) {
        if (venta == null) {
            listaDetalles.clear();
            lblSinDetalles.setVisible(true);
            return;
        }

        lblDetalleCliente.setText("Cliente: " + venta.cliente().nombre());
        lblDetalleFecha.setText("Fecha: " + venta.fechaVenta());
        lblDetalleTotal.setText("Total: €" + venta.importeTotal());

        listaDetalles.setAll(venta.ventaDetalle());
        tablaDetalles.setItems(listaDetalles);

        lblSinDetalles.setVisible(listaDetalles.isEmpty());
    }

    // =========================
    // ELIMINAR VENTA
    // =========================
    @FXML
    private void handleEliminarVenta(ActionEvent e) {

        VentaResponse seleccion = tablaVentas.getSelectionModel().getSelectedItem();

        if (seleccion == null) {
            mostrarAlerta("Seleccione una venta.", Alert.AlertType.WARNING);
            return;
        }

        // ⚠ IDENTIFICAR VENTA REAL EN BD (porque tu DTO NO TIENE ID)
        Venta ventaReal = VentaResponse.toEntity(ventaService.buscarTodos().stream()
                .filter(v ->
                        v.fechaVenta().equals(seleccion.fechaVenta()) &&
                                v.importeTotal().compareTo(seleccion.importeTotal()) == 0 &&
                                v.cliente().dni().equals(seleccion.cliente().dni())
                )
                .findFirst()
                .orElseThrow());

        if (ventaReal == null) {
            mostrarAlerta("No se pudo ubicar la venta real para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Seguro que desea eliminar la venta?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            ventaService.borrar(ventaReal.getId());
            cargarVentas();
            mostrarAlerta("Venta eliminada.", Alert.AlertType.INFORMATION);
        }
    }

    // =========================
    // EDITAR VENTA
    // =========================
    @FXML
    private void handleEditarVenta(ActionEvent e) {
        VentaResponse venta = tablaVentas.getSelectionModel().getSelectedItem();

        if (venta == null) {
            mostrarAlerta("Seleccione una venta para editar.", Alert.AlertType.WARNING);
            return;
        }

        cambiarVistaConDatos(e,"/org/mdv/view/venta/venta-insert.fxml",venta);
    }

    @FXML
    private void handleBackButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/main-window.fxml");
    }
    // =========================
    // NUEVA VENTA
    // =========================
    @FXML
    private void handleNuevaVentaButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/venta/venta-insert.fxml");
    }
}
