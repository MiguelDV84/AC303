package org.mdv.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import org.mdv.dto.CategoriaRequest;
import org.mdv.dto.CategoriaResponse;
import org.mdv.service.CategoriaService;

public class CategoriaEditarController extends WindowControllerBase {

    @FXML private TextField txtNombre;

    @FXML private Label lblResumenCategoria;
    @FXML private Label lblPreviewNombre;
    @FXML private Label lblPreviewProductos;

    private CategoriaResponse categoriaOriginal;

    private final CategoriaService categoriaService = new CategoriaService();

    @FXML
    public void initialize() {
        super.initialize();

        // Listener para vista previa
        txtNombre.textProperty().addListener((obs, oldV, newV) -> actualizarPreview(newV));
    }

    // ======================================================
    //      CARGAR DATOS PARA EDICIÓN
    // ======================================================
    public void cargarCategoria(CategoriaResponse categoria) {

        this.categoriaOriginal = categoria;

        txtNombre.setText(categoria.nombre());

        lblResumenCategoria.setText("Editando categoría: " + categoria.nombre());

        lblPreviewNombre.setText(categoria.nombre());

        actualizarPreview(categoria.nombre());
    }

    // ======================================================
    //      ACTUALIZAR PREVIEW EN VIVO
    // ======================================================
    private void actualizarPreview(String nuevoNombre) {

        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            lblPreviewNombre.setText("[NOMBRE]");
        } else {
            lblPreviewNombre.setText(nuevoNombre);
        }
    }

    // ======================================================
    //     RESTABLECER
    // ======================================================
    @FXML
    private void restablecerCambios() {
        cargarCategoria(categoriaOriginal);
    }

    // ======================================================
    //     GUARDAR
    // ======================================================
    @FXML
    private void guardarCambios(ActionEvent e) {

        if (txtNombre.getText().isBlank()) {
            mostrarAlerta("El nombre no puede estar vacío", Alert.AlertType.WARNING);
            return;
        }

        CategoriaRequest request = new CategoriaRequest(txtNombre.getText());

        categoriaService.actualizar(categoriaOriginal.id(), request);

        mostrarAlerta("Categoría actualizada correctamente", Alert.AlertType.INFORMATION);

        handleBackButton(e);
    }

    // ======================================================
    //     VOLVER
    // ======================================================
    @FXML
    private void handleBackButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/categoria/categoria-listado.fxml");
    }
}
