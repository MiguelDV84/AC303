package org.mdv.view.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import org.mdv.dto.*;
import org.mdv.model.*;
import org.mdv.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VentaInsertController extends WindowControllerBase {

    // ----------- UI FXML -----------
    @FXML private DatePicker fechaVentaPicker;

    // Cliente
    @FXML private TextField txtBuscarCliente;
    @FXML private Label lblNombreCliente;
    @FXML private Label lblDniCliente;
    @FXML private Label lblTelCliente;
    @FXML private Label lblDirCliente;

    private Cliente clienteSeleccionado;

    // Productos disponibles
    @FXML private TextField txtBuscarProducto;
    @FXML private TableView<ProductoResponse> tablaProductos;
    @FXML private TableColumn<ProductoResponse,String> colProdNombre;
    @FXML private TableColumn<ProductoResponse,String> colProdPrecio;
    @FXML private TableColumn<ProductoResponse, String> colProdStock;
    @FXML private TableColumn<ProductoResponse, String> colAccionAdd;
    @FXML private TableColumn<ProductoResponse, String> colAccionRemove;


    // Resumen de venta (productos añadidos)
    @FXML private TableView<VentaDetalleResponse> tablaResumen;
    @FXML private TableColumn<VentaDetalleResponse,String> colResProd;
    @FXML private TableColumn<VentaDetalleResponse,String> colResCant;
    @FXML private TableColumn<VentaDetalleResponse,String> colResPU;
    @FXML private TableColumn<VentaDetalleResponse,String> colResTotal;

    // Totales
    @FXML private Label lblSubtotal;
    @FXML private Label lblIVA;
    @FXML private Label lblTotal;
    @FXML private TextField txtDescuento;

    // ----------- LISTAS -----------
    private final ObservableList<ProductoResponse> listaProductos = FXCollections.observableArrayList();
    private final ObservableList<VentaDetalleResponse> listaResumen = FXCollections.observableArrayList();

    // ----------- SERVICIOS -----------
    private final ClienteService clienteService;
    private final ProductoService productoService;
    private final VentaService ventaService;

    public VentaInsertController() {
        CategoriaService categoriaService = new CategoriaService();
        this.clienteService = new ClienteService();
        this.productoService = new ProductoService(categoriaService);

        VentaDetalleService ventaDetalleService = new VentaDetalleService(null, productoService);
        this.ventaService = new VentaService(clienteService);
    }

    @FXML
    public void initialize() {
        super.initialize();

        fechaVentaPicker.setValue(LocalDate.now());

        inicializarTablaProductos();
        inicializarTablaResumen();

        cargarProductos();
    }

    // ----------------------------------------------------------
    // CONFIG TABLA PRODUCTOS DISPONIBLES
    // ----------------------------------------------------------
    private void inicializarTablaProductos() {

        colProdNombre.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().descripcion()));
        colProdPrecio.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().precioRecomendado().toPlainString()));
        colProdNombre.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().descripcion()));
        colProdPrecio.setCellValueFactory(p -> new ReadOnlyStringWrapper(p.getValue().precioRecomendado().toPlainString()));
        colProdStock.setCellValueFactory(p -> new ReadOnlyStringWrapper(String.valueOf(p.getValue().existencias())));

        tablaProductos.setItems(listaProductos);

        // Columna con botón AÑADIR
        colAccionAdd.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("+");

            {
                btn.setStyle("-fx-background-color:#2ecc71; -fx-text-fill:white;");
                btn.setOnAction(e -> {
                    ProductoResponse p = getTableView().getItems().get(getIndex());
                    anadirProducto(ProductoResponse.toEntity(p));
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        colAccionRemove.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("−");

            {
                btn.setStyle("-fx-background-color:#e74c3c; -fx-text-fill:white;");
                btn.setOnAction(e -> {
                    ProductoResponse p = getTableView().getItems().get(getIndex());
                    quitarProductoPorCodigo(p.codigo());
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });


    }

    private void cargarProductos() {
        listaProductos.clear();
        listaProductos.addAll(productoService.buscarTodos());
    }

    // ----------------------------------------------------------
    // CONFIG TABLA RESUMEN (PRODUCTOS AÑADIDOS)
    // ----------------------------------------------------------
    private void inicializarTablaResumen() {

        colResProd.setCellValueFactory(r -> new ReadOnlyStringWrapper(r.getValue().producto().descripcion()));
        colResCant.setCellValueFactory(r -> new ReadOnlyStringWrapper(String.valueOf(r.getValue().cantidad())));
        colResPU.setCellValueFactory(r -> new ReadOnlyStringWrapper(r.getValue().precioUnitario().toPlainString()));
        colResTotal.setCellValueFactory(r -> new ReadOnlyStringWrapper(r.getValue().totalLinea().toPlainString()));

        tablaResumen.setItems(listaResumen);
    }

    // ----------------------------------------------------------
    // BUSCAR CLIENTE
    // ----------------------------------------------------------
    @FXML
    private void buscarCliente() {
        try {
            ClienteResponse c = clienteService.buscarPorDni(txtBuscarCliente.getText());
            clienteSeleccionado = ClienteResponse.toEntity(c);

            lblNombreCliente.setText("Nombre: " + c.nombre());
            lblDniCliente.setText("DNI: " + c.dni());
            lblTelCliente.setText("Teléfono: " + c.telefono());
            lblDirCliente.setText("Dirección: " + c.dirEnvio());

        } catch (Exception ex) {
            mostrarAlerta("Cliente no encontrado.", Alert.AlertType.ERROR);
        }
    }

    // ----------------------------------------------------------
    // AÑADIR PRODUCTO A LA VENTA
    // ----------------------------------------------------------
    private void anadirProducto(Producto producto) {

        for (VentaDetalleResponse d : listaResumen) {
            if (d.producto().codigo().equals(producto.getCodigo())) {

                int nuevaCantidad = d.cantidad() + 1;
                BigDecimal totalLinea = producto.getPrecioRecomendado()
                        .multiply(BigDecimal.valueOf(nuevaCantidad));

                listaResumen.remove(d);
                listaResumen.add(new VentaDetalleResponse(
                        nuevaCantidad,
                        producto.getPrecioRecomendado(),
                        totalLinea,
                        ProductoResponse.fromEntity(producto)
                ));

                calcularTotales();
                return;
            }
        }

        listaResumen.add(new VentaDetalleResponse(
                1,
                producto.getPrecioRecomendado(),
                producto.getPrecioRecomendado(),
                ProductoResponse.fromEntity(producto)
        ));

        calcularTotales();
    }

    // ----------------------------------------------------------
    // CALCULAR TOTALES
    // ----------------------------------------------------------
    private void calcularTotales() {

        BigDecimal subtotal = listaResumen.stream()
                .map(VentaDetalleResponse::totalLinea)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal iva = subtotal.multiply(BigDecimal.valueOf(0.21));

        BigDecimal descuento = BigDecimal.ZERO;
        try {
            if (!txtDescuento.getText().isBlank())
                descuento = new BigDecimal(txtDescuento.getText());
        } catch (Exception ignored) {}

        BigDecimal total = subtotal.add(iva).subtract(descuento);

        lblSubtotal.setText(subtotal.toPlainString() + " €");
        lblIVA.setText(iva.toPlainString() + " €");
        lblTotal.setText(total.toPlainString() + " €");
    }

    // ----------------------------------------------------------
    // FINALIZAR / GUARDAR VENTA
    // ----------------------------------------------------------
    @FXML
    private void finalizarVenta(ActionEvent e) {
        guardarVenta(e);
    }

    private void guardarVenta(ActionEvent e) {

        if (clienteSeleccionado == null) {
            mostrarAlerta("Seleccione un cliente.", Alert.AlertType.ERROR);
            return;
        }

        if (listaResumen.isEmpty()) {
            mostrarAlerta("Debe añadir productos.", Alert.AlertType.ERROR);
            return;
        }

        BigDecimal total = new BigDecimal(lblTotal.getText().replace(" €",""));

        VentaRequest req = new VentaRequest(
                total,
                fechaVentaPicker.getValue(),
                clienteSeleccionado.getDni(),
                listaResumen.stream()
                        .map(v -> new VentaDetalleRequest(
                                v.cantidad(),
                                v.precioUnitario(),
                                v.totalLinea(),
                                v.producto().codigo(),
                                0
                        ))
                        .toList()

        );

        try {
            ventaService.guardar(req);
            mostrarAlerta("Venta registrada correctamente.", Alert.AlertType.INFORMATION);
            cambiarVista(e, "/org/mdv/view/main-window.fxml");

        } catch (Exception ex) {
            mostrarAlerta("Error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void buscarProducto() {

        String filtro = txtBuscarProducto.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            // Mostrar todos los productos
            tablaProductos.setItems(listaProductos);
            return;
        }

        ObservableList<ProductoResponse> filtrados = FXCollections.observableArrayList(
                listaProductos.stream()
                        .filter(p ->
                                p.codigo().toLowerCase().contains(filtro) ||
                                        p.descripcion().toLowerCase().contains(filtro)
                        )
                        .toList()
        );

        tablaProductos.setItems(filtrados);

        if (filtrados.isEmpty()) {
            mostrarAlerta("No se encontraron productos con ese código o descripción.", Alert.AlertType.INFORMATION);
        }
    }

    // ----------------------------------------------------------
    // BOTONES
    // ----------------------------------------------------------
    @FXML
    private void cancelarVenta(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/main-window.fxml");
    }

    @FXML
    private void guardarBorrador() {
        mostrarAlerta("Funcionalidad no implementada.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void handleBackButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/main-window.fxml");
    }

    private void quitarProductoPorCodigo(String codigo) {

        for (VentaDetalleResponse d : listaResumen) {

            if (d.producto().codigo().equals(codigo)) {

                if (d.cantidad() > 1) { // Restar uno
                    int nuevaCant = d.cantidad() - 1;
                    BigDecimal totalLinea = d.precioUnitario()
                            .multiply(BigDecimal.valueOf(nuevaCant));

                    VentaDetalleResponse nuevo = new VentaDetalleResponse(
                            nuevaCant,
                            d.precioUnitario(),
                            totalLinea,
                            d.producto()
                    );

                    listaResumen.remove(d);
                    listaResumen.add(nuevo);

                } else { // Eliminar línea completa
                    listaResumen.remove(d);
                }

                tablaProductos.refresh();
                calcularTotales();
                return;
            }
        }

        mostrarAlerta("Este producto no está en la venta.", Alert.AlertType.WARNING);
    }

}
