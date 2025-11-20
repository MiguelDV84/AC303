package org.mdv.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/mdv/view/main-window.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 720);

        // ❗ Esto oculta los botones de Windows
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);
        stage.setTitle("Gestión de Clientes");
        stage.show();
    }
}
