package org.mdv.view.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainWindowController extends WindowControllerBase {

    @FXML
    private HBox windowButtons;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        super.initialize();
        // ESPERAR A QUE LA ESCENA EXISTA → SIN ESTO NO FUNCIONA
        Platform.runLater(() -> {
            Stage stage = (Stage) windowButtons.getScene().getWindow();

            // ARRASRAR VENTANA
            windowButtons.setOnMousePressed(e -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });

            windowButtons.setOnMouseDragged(e -> {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            });
        });
    }

    // ========================================================================
    // ======================   CAMBIAR VISTAS   ===============================
    // ========================================================================

    public void cambiarVista(ActionEvent event, String fxml) {
        try {
            Parent nuevaVista = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource(fxml))
            );

            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(nuevaVista);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ========================================================================
    // ======================   CLIENTES   ====================================
    // ========================================================================

    @FXML
    private void handleInsertarCliente(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/cliente/cliente-insert.fxml");
    }

    @FXML
    private void handleVerClientes(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/cliente/cliente-listado.fxml");
    }

    // ========================================================================
    // ======================   PRODUCTOS   ===================================
    // ========================================================================

    @FXML
    private void handleVerProductos(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/producto/producto-listado.fxml");
    }

    // ========================================================================
    // ======================   CATEGORÍAS   ==================================
    // ========================================================================

    @FXML
    private void handleCategorias(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/categoria/categoria-listado.fxml");
    }

    // ========================================================================
    // ======================   VENTAS   ======================================
    // ========================================================================

    @FXML
    private void handleVentas(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/venta/venta-listado.fxml");
    }

    // ========================================================================
    // ======================   REPORTES   ====================================
    // ========================================================================

    @FXML
    private void handleReportes(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/reporte/reporte.fxml");
    }

    // ========================================================================
    // ======================   CONFIGURACIÓN   ================================
    // ========================================================================

    @FXML
    private void handleConfiguracion(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/config/configuracion.fxml");
    }

    // ========================================================================
    // ======================   BOTONES VENTANA   ==============================
    // ========================================================================

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) windowButtons.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizarVentana() {
        Stage stage = (Stage) windowButtons.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void maximizarVentana() {
        Stage stage = (Stage) windowButtons.getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }
}
