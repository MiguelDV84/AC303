package org.mdv.view.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.mdv.dto.VentaDetalleResponse;
import org.mdv.dto.VentaResponse;
import org.mdv.service.*;
public class VentaListadoController extends WindowControllerBase {

    @FXML private TableView<VentaResponse> tablaVentas;
    @FXML private TableColumn<VentaResponse, String> colFecha;
    @FXML private TableColumn<VentaResponse, String> colCliente;
    @FXML private TableColumn<VentaResponse, String> colTotal;

    @FXML private TableView<VentaDetalleResponse> tablaDetalles;
    @FXML private TableColumn<VentaDetalleResponse, String> colProd;
    @FXML private TableColumn<VentaDetalleResponse, String> colCant;
    @FXML private TableColumn<VentaDetalleResponse, String> colPrecio;
    @FXML private TableColumn<VentaDetalleResponse, String> colLinea;

    @FXML private Label lblDetalleCliente;
    @FXML private Label lblDetalleFecha;
    @FXML private Label lblDetalleTotal;
    @FXML private Label lblSinDetalles;

    private final VentaService ventaService;

    private final ObservableList<VentaResponse> listaVentas = FXCollections.observableArrayList();
    private final ObservableList<VentaDetalleResponse> listaDetalles = FXCollections.observableArrayList();

    public VentaListadoController() {

        ClienteService clienteService = new ClienteService();
        CategoriaService categoriaService = new CategoriaService();
        ProductoService productoService = new ProductoService(categoriaService);

        // primero creamos VentaService sin detalles
        VentaService ventaServiceTemp = new VentaService(clienteService, null);

        // ahora VentaDetalleService necesita VentaService y ProductoService
        VentaDetalleService ventaDetalleService = new VentaDetalleService(ventaServiceTemp, productoService);

        // Y ahora sustituimos el null que tenía VentaService
        this.ventaService = new VentaService(clienteService, ventaDetalleService);
    }

    @FXML
    public void initialize() {
        super.initialize();

        colFecha.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().fechaVenta().toString()));
        colCliente.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().cliente().nombre()));
        colTotal.setCellValueFactory(v -> new ReadOnlyStringWrapper(v.getValue().importeTotal().toPlainString()));

        colProd.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().producto().descripcion()));
        colCant.setCellValueFactory(d -> new ReadOnlyStringWrapper(String.valueOf(d.getValue().cantidad())));
        colPrecio.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().precioUnitario().toPlainString()));
        colLinea.setCellValueFactory(d -> new ReadOnlyStringWrapper(d.getValue().totalLinea().toPlainString()));

        cargarVentas();

        tablaVentas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, nuevaVenta) -> mostrarDetalles(nuevaVenta)
        );
    }

    private void cargarVentas() {
        listaVentas.setAll(ventaService.buscarTodos());
        tablaVentas.setItems(listaVentas);
    }

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

    @FXML
    private void handleBackButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/main-window.fxml");
    }
}
