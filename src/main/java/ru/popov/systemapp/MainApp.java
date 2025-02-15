package ru.popov.systemapp;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.bootstrapfx.BootstrapFX;
import ru.popov.systemapp.dialog.AlertCloseMain;
import ru.popov.systemapp.dialog.AlertException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;


public class MainApp extends Application {

    private static final Logger logger = LogManager.getLogger(MainApp.class);
    @Getter
    public static Connection connection = null;


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        try{
            stage.getIcons().add(new Image(getClass().getResourceAsStream("img/journal.ico")));
            AlertCloseMain.closeWindow(stage);
        }catch (Exception e){
            AlertException.alertException("Не возможно получить доступ к иконке");
            logger.error(e.getMessage());
        }
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        scene.getStylesheets().add(getClass().getResource("style/main-view-style.css").toExternalForm());
        stage.setResizable(false);
        stage.setTitle("Электронный журнал");
        stage.setScene(scene);
        stage.show();

        this.notifyPreloader(new Preloader.StateChangeNotification(null));
    }

    @Override
    public void init() throws Exception {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:./system_app.db");
        connection.setAutoCommit(true);
    }

    @Override
    public void stop() throws Exception {
        connection.close();
    }

    public static void main(String[] args) {
        launch();
    }
}