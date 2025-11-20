package org.mdv.view.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import org.mdv.dto.ProductoRequest;
import org.mdv.dto.ProductoResponse;
import org.mdv.service.ProductoService;
import org.mdv.service.CategoriaService;

public class ProductoListadoController extends WindowControllerBase {

    @FXML private TableView<ProductoResponse> tablaProductos;
    @FXML private TableColumn<ProductoResponse, String> colCodigo;
    @FXML private TableColumn<ProductoResponse, String> colDescripcion;
    @FXML private TableColumn<ProductoResponse, String> colPrecio;
    @FXML private TableColumn<ProductoResponse, String> colStock;
    @FXML private TableColumn<ProductoResponse, String> colCategoria;

    @FXML private TextField txtFiltro;
    @FXML private Label lblTotal;

    private final ObservableList<ProductoResponse> listaProductos = FXCollections.observableArrayList();
    private final ProductoService productoService = new ProductoService(new CategoriaService());

    @FXML
    public void initialize() {
        super.initialize();

        colCodigo.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().codigo()));
        colDescripcion.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().descripcion()));
        colPrecio.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().precioRecomendado().toString()));
        colStock.setCellValueFactory(c -> new ReadOnlyStringWrapper(String.valueOf(c.getValue().existencias())));
        colCategoria.setCellValueFactory(c -> new ReadOnlyStringWrapper(
                c.getValue().categoria() != null ? c.getValue().categoria().nombre() : "Sin categoría"
        ));

        obtenerProductos();
    }


    // ======================= CRUD =======================

    @FXML
    public void obtenerProductos() {
        listaProductos.setAll(productoService.buscarTodos());
        tablaProductos.setItems(listaProductos);
        lblTotal.setText("Total: " + listaProductos.size() + " productos");
    }

    @FXML
    public void filtrarPorCodigo() {

        String codigo = txtFiltro.getText().trim();

        if (codigo.isEmpty()) {
            obtenerProductos();
            return;
        }

        ProductoResponse prod = productoService.buscarPorCodigo(codigo);

        if (prod == null) {
            listaProductos.clear();
        } else {
            listaProductos.setAll(prod);
        }

        tablaProductos.setItems(listaProductos);
    }



    @FXML
    public void filtrarPorNombre() {
        String filtro = txtFiltro.getText().toLowerCase();
        if (!filtro.isEmpty()) {
            listaProductos.setAll(productoService.buscarPorNombre(filtro));
        } else {
            obtenerProductos();
        }
    }


    @FXML
    public void handleNuevoProducto(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/producto/producto-insert.fxml");
    }

    @FXML
    public void handleEliminarProducto() {

        ProductoResponse producto = tablaProductos.getSelectionModel().getSelectedItem();

        if (producto == null) {
            mostrarAlerta("Debe seleccionar un producto para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        // Confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar Producto");
        confirmacion.setHeaderText("¿Seguro que deseas eliminar este producto?");
        confirmacion.setContentText(
                "Producto: " + producto.descripcion() +
                        "\nCódigo: " + producto.codigo()
        );

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {

                try {
                    productoService.borrar(producto.codigo());
                    obtenerProductos();
                    mostrarAlerta("Producto eliminado correctamente.", Alert.AlertType.INFORMATION);

                } catch (Exception ex) {
                    mostrarAlerta(
                            "No se puede eliminar este producto porque está asociado a ventas.",
                            Alert.AlertType.ERROR
                    );
                }
            }
        });
    }

    @FXML
    private void handleEditarProducto(ActionEvent e) {

        ProductoResponse producto = tablaProductos.getSelectionModel().getSelectedItem();

        if (producto == null) {
            mostrarAlerta("Debe seleccionar un producto para editar.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/mdv/view/producto/producto-editar.fxml"));
            Parent root = loader.load();

            // Obtener controller y pasar datos
            ProductoEditarController controller = loader.getController();
            controller.cargarProducto(producto);

            // Mostrar escena
            Scene scene = ((Control) e.getSource()).getScene();
            scene.setRoot(root);

        } catch (Exception ex) {
            ex.printStackTrace();
            mostrarAlerta("Error al abrir la ventana de edición: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void handleBackButton(ActionEvent event) {
        cambiarVista(event, "/org/mdv/view/main-window.fxml");
    }
}
