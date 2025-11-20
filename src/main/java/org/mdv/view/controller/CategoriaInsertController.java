package org.mdv.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import org.mdv.dto.CategoriaRequest;
import org.mdv.service.CategoriaService;

public class CategoriaInsertController extends WindowControllerBase {

    @FXML private TextField txtNombre;
    @FXML private TextField txtBuscar; // este existe en el FXML

    private final CategoriaService categoriaService = new CategoriaService();

    @FXML
    public void initialize() {
        super.initialize();
    }

    // ==============================
    //      GUARDAR CATEGORÍA
    // ==============================
    @FXML
    private void guardarCategoria(ActionEvent e) {

        if (txtNombre.getText().isEmpty()) {
            mostrarAlerta("El nombre de la categoría es obligatorio.", Alert.AlertType.WARNING);
            return;
        }

        CategoriaRequest nueva = new CategoriaRequest(
                txtNombre.getText()
        );

        categoriaService.guardar(nueva);

        mostrarAlerta("Categoría guardada correctamente.", Alert.AlertType.INFORMATION);

        handleBackButton(e);
    }

    // ==============================
    //      LIMPIAR FORMULARIO
    // ==============================
    @FXML
    private void limpiarFormulario() {
        txtNombre.clear();
    }

    // ==============================
    //      FILTRAR CATEGORÍAS (REQUIRED BY FXML)
    // ==============================
    @FXML
    private void filtrarCategorias() {
        // Más adelante implementamos esto
        // Por ahora debe existir para que el FXML cargue
    }

    // ==============================
    //      VOLVER
    // ==============================
    @FXML
    private void handleBackButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/categoria/categoria-listado.fxml");
    }
}
