package ru.popov.systemapp.service;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.bootstrapfx.BootstrapFX;
import ru.popov.systemapp.MainApp;
import ru.popov.systemapp.dialog.AlertCloseWindow;
import ru.popov.systemapp.dialog.AlertException;

import java.io.IOException;

public class OpenScene {
    private static final Logger logger = LogManager.getLogger(OpenScene.class);
    public static void openScene(String window, String title, String styleUrl, String imgUrl){
        Stage stage = new Stage();
        stage.setTitle(title);
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(window));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(MainApp.class.getResource(styleUrl).toExternalForm());
            stage.getIcons().add(new Image(MainApp.class.getResourceAsStream(imgUrl)));
            stage.setResizable(false);
            AlertCloseWindow.closeWindow(stage);
            stage.setScene(scene);
            stage.show();

        } catch (IOException | IllegalStateException ex) {
            AlertException.alertException(ex.getMessage());
            logger.error(ex.getMessage());
        }
    }
}
