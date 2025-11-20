package org.mdv.view.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public abstract class WindowControllerBase {

    @FXML protected HBox topBar;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    protected void initialize() {
        configurarMovimientoVentana();
    }

    private void configurarMovimientoVentana() {

        Platform.runLater(() -> {

            if (topBar == null) {
                System.out.println("⚠ topBar no encontrado en el FXML: no se podrá mover la ventana.");
                return;
            }

            Stage stage = (Stage) topBar.getScene().getWindow();

            topBar.setOnMousePressed(e -> {
                xOffset = e.getSceneX();
                yOffset = e.getSceneY();
            });

            topBar.setOnMouseDragged(e -> {
                stage.setX(e.getScreenX() - xOffset);
                stage.setY(e.getScreenY() - yOffset);
            });
        });
    }

    // =============================================================
    // BOTONES VENTANA PERSONALIZADA (Cerrar / Minimizar / Maximizar)
    // =============================================================

    @FXML
    protected void cerrarVentana(MouseEvent e) {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void minimizarVentana(MouseEvent e) {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void maximizarVentana(MouseEvent e) {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setMaximized(!stage.isMaximized());
    }

    // =============================================================
    // CAMBIO DE ESCENA BÁSICO
    // =============================================================

    protected void cambiarVista(javafx.event.ActionEvent e, String ruta) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(ruta));
            javafx.scene.Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =============================================================
    // CAMBIO DE ESCENA PASANDO DATOS A OTRO CONTROLLER
    // =============================================================

    protected void cambiarVistaConDatos(javafx.event.ActionEvent e, String ruta, Object datos) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource(ruta));
            javafx.scene.Parent root = loader.load();

            Object controller = loader.getController();

            // ===========================================
            //   CLIENTES
            // ===========================================
            if (controller instanceof ClienteEditarController editarCtrl &&
                    datos instanceof org.mdv.dto.ClienteRequest clienteRequest) {
                editarCtrl.cargarCliente(clienteRequest);
            }

            // ===========================================
            //   CATEGORÍAS   ✅ AÑADIDO
            // ===========================================
            if (controller instanceof CategoriaEditarController editarCatCtrl &&
                    datos instanceof org.mdv.dto.CategoriaResponse categoriaResponse) {
                editarCatCtrl.cargarCategoria(categoriaResponse);
            }

            // Aplicar cambio de escena
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    protected void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

}
