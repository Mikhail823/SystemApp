package ru.popov.systemapp;

import javafx.application.Application;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreloaderApp extends Preloader {
    private Stage window;
    private static final Logger logger = LogManager.getLogger(PreloaderApp.class);


    public static void main(String[] args) {
        System.setProperty("javafx.preloader", "ru.popov.systemapp.PreloaderApp");
        logger.info("Загрузка приложения.......");
        Application.launch(MainApp.class, args);
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof StateChangeNotification){
            this.window.close();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Image image = null;
        try {
            image = new Image(getClass().getResourceAsStream("img/kosmos.jpeg"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        window = stage;
        Scene scene = new Scene(new VBox(new ImageView(image)));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
}
