package org.mdv.view.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import org.mdv.dto.ClienteRequest;
import org.mdv.service.ClienteService;

import java.io.IOException;
import java.util.Objects;

public class ClienteInsertController extends WindowControllerBase {

    // ====== CAMPOS FXML ======
    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDirHabitual;
    @FXML private TextField txtDirEnvio;

    private final ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
        super.initialize(); // activa el movimiento de ventana
    }

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

    // ==================== BOTÓN VOLVER ====================
    @FXML
    private void handleBackButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/main-window.fxml");
    }

    // ==================== COPIAR DIRECCIÓN ====================
    @FXML
    private void copiarDireccion() {
        txtDirEnvio.setText(txtDirHabitual.getText());
    }

    // ==================== LIMPIAR FORMULARIO ====================
    @FXML
    private void limpiarCampos() {
        txtDni.clear();
        txtNombre.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        txtDirHabitual.clear();
        txtDirEnvio.clear();
    }

    // ==================== GUARDAR CLIENTE ====================
    @FXML
    private void guardarCliente(ActionEvent e) {

        if (txtDni.getText().isEmpty() ||
                txtNombre.getText().isEmpty() ||
                txtApellidos.getText().isEmpty() ||
                txtDirEnvio.getText().isEmpty()) {

            System.out.println("❌ Faltan campos obligatorios");
            return;
        }

        ClienteRequest cliente = new ClienteRequest(
                txtDni.getText(),
                txtNombre.getText(),
                txtApellidos.getText(),
                txtTelefono.getText(),
                txtDirHabitual.getText(),
                txtDirEnvio.getText()
        );

        clienteService.guardar(cliente);
        System.out.println("✔ Cliente creado correctamente");

        cambiarVista(e, "/org/mdv/view/cliente/cliente-listado.fxml");
    }

}
