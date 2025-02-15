package ru.popov.systemapp.dialog;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

public class AlertInfo {

    public static Alert info(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Иформация!");
        alert.setContentText(msg);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();
        return alert;
    }
}
