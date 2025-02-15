package ru.popov.systemapp.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.popov.systemapp.MainApp;

import java.util.Optional;

public class AlertCloseMain {
    private static final Logger logger = LogManager.getLogger(AlertCloseMain.class);

    public static void closeWindow(Stage stage) {
        stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, e -> {
            ButtonType ok = new ButtonType("Завершить", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Не завершать", ButtonBar.ButtonData.NO);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Вы точно собираетесь завершить работу?", ok, no);
            alert.setTitle("Закрытие приложения");
            alert.setHeaderText(null);
            alert.setGraphic(new ImageView(new Image(MainApp.class.getResourceAsStream("img/change.png"))));
            alert.setContentText("Завершить работу?");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initStyle(StageStyle.UTILITY);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ok) {
                stage.getOnCloseRequest();
                logger.info("Завершение работы приложения........");
            } else {
                e.consume();
            }
        });
    }
}
