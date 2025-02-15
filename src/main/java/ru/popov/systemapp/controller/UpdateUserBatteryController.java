package ru.popov.systemapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Setter;
import net.synedra.validatorfx.Validator;
import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.dialog.AlertException;
import ru.popov.systemapp.dto.JournalFXDTO;
import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Battery;
import ru.popov.systemapp.model.Journal;
import ru.popov.systemapp.util.OperationAction;


import java.sql.SQLException;

public class UpdateUserBatteryController {

    @FXML
    private TextField inputCode;

    @Setter
    private JournalFXDTO journalFXDTO;

    @FXML
    protected void initialize()  {
        inputCode.setFocusTraversable(true);
        inputCode.requestFocus();
        isValidateInput();
    }


    @FXML
    private void batteryReplacement() throws SQLException, DaoException {
        if (journalFXDTO == null){
            AlertException.alertException("Вы не выбрали сотрудника для замены АКБ!!!!");
        } else {
            Journal journal = FactoryDAO
                    .getInstance().getJournalDAO().findEntityById(journalFXDTO.getId().getValue());
            journal.setBattery(inputCode.getText());
            FactoryDAO.getInstance().getJournalDAO().update(journal);
            Battery battery = FactoryDAO.getInstance().getBatteryDAO().findBatteryByCode(inputCode.getText());
            battery.setStatus(OperationAction.ON_THE_ISSUE.toString());
            FactoryDAO.getInstance().getBatteryDAO().updateBattery(battery);
            inputCode.clear();
        }
    }

    private void isValidateInput(){
        Validator validator = new Validator();
        validator.createCheck().dependsOn("714-AK", inputCode.textProperty())
                .withMethod(c -> {
                   String code = c.get("714-AK");
                   if (!code.toUpperCase().equals(code)){
                       c.error("Не правильно указан код!");
                   }
                }).decorates(inputCode).immediate();
    }
}
