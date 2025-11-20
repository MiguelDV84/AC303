package org.mdv.view.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.mdv.dto.ClienteRequest;
import org.mdv.dto.ClienteResponse;
import org.mdv.service.ClienteService;

import java.io.IOException;
import java.util.Objects;

public class ClienteListadoController extends WindowControllerBase {

    @FXML private TableView<ClienteResponse> tablaClientes;
    @FXML private TableColumn<ClienteResponse, String> colDni;
    @FXML private TableColumn<ClienteResponse, String> colNombre;
    @FXML private TableColumn<ClienteResponse, String> colApellidos;
    @FXML private TableColumn<ClienteResponse, String> colTelefono;
    @FXML private TableColumn<ClienteResponse, String> colDirHabitual;
    @FXML private TableColumn<ClienteResponse, String> colDirEnvio;

    @FXML private TextField txtFiltro;

    private final ObservableList<ClienteResponse> listaClientes = FXCollections.observableArrayList();
    private final ClienteService clienteService = new ClienteService();

    @FXML
    public void initialize() {
        super.initialize(); // Activa la barra superior

        // Configurar columnas
        colDni.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().dni()));
        colNombre.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nombre()));
        colApellidos.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().apellidos()));
        colTelefono.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().telefono()));
        colDirHabitual.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().dirHabitual()));
        colDirEnvio.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().dirEnvio()));

        obtenerClientes();
    }




    public void cambiarVista(ActionEvent event, String fxml) {
        try {
            Parent nuevaVista = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
            Scene scene = ((Node) event.getSource()).getScene();
            scene.setRoot(nuevaVista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void obtenerClientes() {
        listaClientes.setAll(clienteService.buscarTodos());
        tablaClientes.setItems(listaClientes);
    }

    @FXML
    public void filtrarPorDni() {
        String filtro = txtFiltro.getText().toLowerCase();
        if (!filtro.isEmpty()) {
            listaClientes.setAll(clienteService.buscarPorDni(filtro));
        } else {
            obtenerClientes();
        }
    }

    @FXML
    public void filtrarPorNombre() {
        String filtro = txtFiltro.getText().toLowerCase();
        if (!filtro.isEmpty()) {
            listaClientes.setAll(clienteService.buscarPorNombre(filtro));
        } else {
            obtenerClientes();
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/main-window.fxml");
    }

    @FXML
    public void handleNuevoCliente(ActionEvent event) {cambiarVista(event, "/org/mdv/view/cliente/cliente-insert.fxml");}

    @FXML
    public void handleEliminarCliente() {

        ClienteResponse cliente = tablaClientes.getSelectionModel().getSelectedItem();

        if (cliente == null) {
            mostrarAlerta("Debe seleccionar un cliente para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Cliente");
        confirmacion.setHeaderText("¿Seguro que deseas eliminar este cliente?");
        confirmacion.setContentText("Cliente: " + cliente.nombre() + " " + cliente.apellidos() +
                "\nDNI: " + cliente.dni());

        // Si el usuario confirma
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                clienteService.delete(cliente.dni());
                obtenerClientes(); // Recargar tabla
                mostrarAlerta("Cliente eliminado correctamente.", Alert.AlertType.INFORMATION);
            }
        });
    }


    @FXML
    private void handleEditarCliente(ActionEvent e) {

        ClienteResponse cliente = tablaClientes.getSelectionModel().getSelectedItem();

        if (cliente == null) {
            System.out.println("⚠ No hay cliente seleccionado");
            return;
        }

        // Convertimos ClienteResponse en ClienteRequest
        ClienteRequest request = new ClienteRequest(
                cliente.dni(),
                cliente.nombre(),
                cliente.apellidos(),
                cliente.telefono(),
                cliente.dirHabitual(),
                cliente.dirEnvio()
        );

        cambiarVistaConDatos(
                e,
                "/org/mdv/view/cliente/cliente-editar.fxml",
                request
        );
    }


}
