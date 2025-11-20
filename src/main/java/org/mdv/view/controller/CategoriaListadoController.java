package org.mdv.view.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import org.mdv.dto.CategoriaResponse;
import org.mdv.service.CategoriaService;

public class CategoriaListadoController extends WindowControllerBase {

    @FXML
    private TableView<CategoriaResponse> tablaCategorias;

    @FXML
    private TableColumn<CategoriaResponse, String> colId;

    @FXML
    private TableColumn<CategoriaResponse, String> colNombre;

    @FXML
    private TextField txtFiltro;

    @FXML
    private Label lblTotalCategorias;

    private final ObservableList<CategoriaResponse> listaCategorias = FXCollections.observableArrayList();
    private final CategoriaService categoriaService = new CategoriaService();

    @FXML
    public void initialize() {
        super.initialize();

        colId.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().id())));
        colNombre.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().nombre()));

        obtenerCategorias();
    }

    // ======================================================
    //      CARGAR LISTA COMPLETA
    // ======================================================
    @FXML
    public void obtenerCategorias() {
        listaCategorias.setAll(categoriaService.buscarTodos());
        tablaCategorias.setItems(listaCategorias);
        lblTotalCategorias.setText("Total: " + listaCategorias.size() + " categorías");
    }

    // ======================================================
    //      FILTRO UNIFICADO (ID o NOMBRE)
    // ======================================================
    @FXML
    public void filtrar() {
        String filtro = txtFiltro.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            obtenerCategorias();
            return;
        }

        try {
            long id = Long.parseLong(filtro);
            CategoriaResponse c = categoriaService.buscarPorId(id);

            listaCategorias.setAll(c != null ? FXCollections.observableArrayList(c) : FXCollections.observableArrayList());

        } catch (NumberFormatException e) {
            listaCategorias.setAll(categoriaService.buscarPorNombre(filtro));
        }
    }


    // ======================================================
    //      NUEVA CATEGORÍA
    // ======================================================
    @FXML
    private void handleNuevaCategoria(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/categoria/categoria-insert.fxml");
    }

    // ======================================================
    //      EDITAR CATEGORÍA
    // ======================================================
    @FXML
    private void handleEditarCategoria(ActionEvent e) {
        CategoriaResponse cat = tablaCategorias.getSelectionModel().getSelectedItem();

        if (cat == null) {
            mostrarAlerta("Debe seleccionar una categoría.", Alert.AlertType.WARNING);
            return;
        }

        cambiarVistaConDatos(
                e,
                "/org/mdv/view/categoria/categoria-editar.fxml",
                cat
        );
    }

    // ======================================================
    //      ELIMINAR CATEGORÍA
    // ======================================================
    @FXML
    public void handleEliminarCategoria() {

        CategoriaResponse seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Debe seleccionar una categoría.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setHeaderText("¿Eliminar categoría?");
        confirm.setContentText("Categoría: " + seleccionada.nombre());

        confirm.showAndWait().ifPresent(resp -> {
            if (resp == ButtonType.OK) {
                try {
                    categoriaService.borrar(seleccionada.id());
                    obtenerCategorias();

                } catch (Exception ex) {
                    mostrarAlerta(
                            "No se puede eliminar la categoría porque tiene productos asociados.",
                            Alert.AlertType.ERROR
                    );
                }
            }
        });
    }


    // ======================================================
    //      VOLVER
    // ======================================================
    @FXML
    public void handleBackButton(ActionEvent e) {
        cambiarVista(e, "/org/mdv/view/main-window.fxml");
    }
}
