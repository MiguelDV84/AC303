package org.mdv.view.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.mdv.dto.CategoriaResponse;
import org.mdv.dto.ProductoRequest;
import org.mdv.service.CategoriaService;
import org.mdv.service.ProductoService;

import java.math.BigDecimal;
import java.util.List;

public class ProductoInsertController extends WindowControllerBase {

    @FXML private TextField txtCodigo;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtExistencias;

    @FXML private ComboBox<CategoriaResponse> comboCategoria;

    private ToggleGroup stockGroup;

    private final ProductoService productoService = new ProductoService(new CategoriaService());
    private final CategoriaService categoriaService = new CategoriaService();

    @FXML
    public void initialize() {
        super.initialize();
        cargarCategorias();
    }


    // =============================================================
    // CARGAR CATEGORÍAS
    // =============================================================
    private void cargarCategorias() {
        List<CategoriaResponse> categorias = categoriaService.buscarTodos();
        comboCategoria.setItems(FXCollections.observableArrayList(categorias));
    }

    // =============================================================
    // GUARDAR PRODUCTO
    // =============================================================
    @FXML
    private void guardarProducto() {

        if (!validarCampos()) {
            mostrarAlerta("Debe completar todos los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            ProductoRequest request = new ProductoRequest(
                    txtCodigo.getText(),
                    txtDescripcion.getText(),
                    new BigDecimal(txtPrecio.getText()),
                    Integer.parseInt(txtExistencias.getText()),
                    comboCategoria.getValue().id()
            );

            productoService.guardar(request);

            mostrarAlerta("Producto guardado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();

        } catch (Exception e) {
            mostrarAlerta("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // =============================================================
    // VALIDAR CAMPOS
    // =============================================================
    private boolean validarCampos() {
        return !txtCodigo.getText().isEmpty()
                && !txtDescripcion.getText().isEmpty()
                && !txtPrecio.getText().isEmpty()
                && !txtExistencias.getText().isEmpty()
                && comboCategoria.getValue() != null;
    }

    // =============================================================
    // LIMPIAR CAMPOS
    // =============================================================
    @FXML
    private void limpiarCampos() {
        txtCodigo.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtExistencias.clear();
        comboCategoria.getSelectionModel().clearSelection();
    }

    // =============================================================
    // VOLVER
    // =============================================================
    @FXML
    private void handleBackButton(ActionEvent e) {
        cambiarVista(e,"/org/mdv/view/producto/producto-listado.fxml");
    }
}
