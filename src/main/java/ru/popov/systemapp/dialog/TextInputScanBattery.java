package ru.popov.systemapp.dialog;

import javafx.scene.control.ChoiceDialog;
import javafx.stage.Modality;

public class TextInputScanBattery {

    public static ChoiceDialog<String> scanDialogBattery(){
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("Выдать", "На разряд", "На заряд");
        choiceDialog.setTitle("Отсканирован аккумулятор!");
        choiceDialog.setHeaderText("Выберите действие");
        choiceDialog.initModality(Modality.WINDOW_MODAL);
        choiceDialog.showAndWait();
        return choiceDialog;
    }
}
