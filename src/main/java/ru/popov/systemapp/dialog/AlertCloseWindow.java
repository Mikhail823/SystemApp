package ru.popov.systemapp.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class AlertCloseWindow {

    public static void closeWindow(Stage stage){
        stage.setOnCloseRequest(e -> {
            ButtonType ok = new ButtonType("Закрыть", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("Не закрывать", ButtonBar.ButtonData.NO);
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                    "Вы действительно хотите закрыть окно?", ok, no);
            dialog.setTitle("Закрытие окна");
            dialog.setHeaderText(null);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ok){
                stage.getOnCloseRequest();
            } else {
                e.consume();
            }
        });

    }
}
