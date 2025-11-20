package org.mdv.view.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.mdv.dto.*;
import org.mdv.service.ProductoService;
import org.mdv.service.CategoriaService;

public class ProductoEditarController extends WindowControllerBase {

    @FXML private Label lblResumenProducto;
    @FXML private Label lblCategoriaProducto;

    @FXML private TextField txtCodigo;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtExistencias;

    @FXML private ComboBox<CategoriaResponse> comboCategoria;

    private ProductoResponse productoOriginal;

    private final CategoriaService categoriaService = new CategoriaService();
    private final ProductoService productoService = new ProductoService(new CategoriaService());

    @FXML
    public void initialize() {
        super.initialize();
        cargarCategorias();
    }

    private void cargarCategorias() {
        comboCategoria.setItems(
                FXCollections.observableArrayList(categoriaService.buscarTodos())
        );

        // para que se muestre el nombre en el combo
        comboCategoria.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(CategoriaResponse item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.nombre());
            }
        });

        comboCategoria.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(CategoriaResponse item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.nombre());
            }
        });
    }

    // =============================================================
    //   CARGAR PRODUCTO PARA EDICIÓN
    // =============================================================
    public void cargarProducto(ProductoResponse producto) {
        this.productoOriginal = producto;

        txtCodigo.setText(producto.codigo());
        txtCodigo.setEditable(false);

        txtDescripcion.setText(producto.descripcion());
        txtPrecio.setText(producto.precioRecomendado().toString());
        txtExistencias.setText(String.valueOf(producto.existencias()));

        comboCategoria.getSelectionModel().select(producto.categoria());

        lblResumenProducto.setText(
                "Editando producto: " + producto.descripcion() +
                        "  (Código: " + producto.codigo() + ")"
        );

        lblCategoriaProducto.setText("Categoría: " + producto.categoria().nombre());
    }

    // =============================================================
    //   RESTABLECER
    // =============================================================
    @FXML
    private void restablecerCambios() {
        cargarProducto(productoOriginal);
    }

    // =============================================================
    //   GUARDAR
    // =============================================================
    @FXML
    private void guardarCambios(ActionEvent e) {

        try {
            CategoriaResponse categoria = comboCategoria.getValue();

            ProductoRequest actualizado = new ProductoRequest(
                    txtCodigo.getText(),
                    txtDescripcion.getText(),
                    new java.math.BigDecimal(txtPrecio.getText()),
                    Integer.parseInt(txtExistencias.getText()),
                    categoria.id()
            );

            productoService.actualizar(actualizado);

            mostrarAlerta("Producto actualizado correctamente.", Alert.AlertType.INFORMATION);
            handleBackButton(e);

        } catch (Exception ex) {
            mostrarAlerta("Error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // =============================================================
    //   VOLVER
    // =============================================================
    @FXML
    private void handleBackButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/producto/producto-listado.fxml");
    }

}
