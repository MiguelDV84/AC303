package org.mdv.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.mdv.dto.ClienteRequest;
import org.mdv.service.ClienteService;

public class ClienteEditarController extends WindowControllerBase {

    @FXML private Label lblResumenCliente;

    @FXML private TextField txtDni;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtDirHabitual;
    @FXML private TextField txtDirEnvio;

    private ClienteRequest clienteOriginal;
    private final ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
        super.initialize();
    }

    public void cargarCliente(ClienteRequest cliente) {
        this.clienteOriginal = cliente;

        txtDni.setText(cliente.dni());
        txtNombre.setText(cliente.nombre());
        txtApellidos.setText(cliente.apellidos());
        txtTelefono.setText(cliente.telefono());
        txtDirHabitual.setText(cliente.dirHabitual());
        txtDirEnvio.setText(cliente.dirEnvio());

        lblResumenCliente.setText(
                "Editando cliente: " + cliente.nombre() + " " + cliente.apellidos()
                        + "  (DNI: " + cliente.dni() + ")"
        );
    }

    @FXML
    private void copiarDireccion() {
        txtDirEnvio.setText(txtDirHabitual.getText());
    }

    @FXML
    private void guardarCambios(ActionEvent e) {

        ClienteRequest actualizado = new ClienteRequest(
                txtDni.getText(),
                txtNombre.getText(),
                txtApellidos.getText(),
                txtTelefono.getText(),
                txtDirHabitual.getText(),
                txtDirEnvio.getText()
        );

        clienteService.update(actualizado);
        handleBackButton(e);
    }

    @FXML
    private void verHistorial() {
        System.out.println("Historial no implementado aún.");
    }

    @FXML
    private void handleBackButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/cliente/cliente-listado.fxml");
    }
}
