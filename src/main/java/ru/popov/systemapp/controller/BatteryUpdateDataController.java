package ru.popov.systemapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.popov.systemapp.Preloader;
import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.dialog.AlertSucceeded;
import ru.popov.systemapp.dto.BatteryFXDTO;
import ru.popov.systemapp.model.Battery;

import java.sql.SQLException;


public class BatteryUpdateDataController {

    @FXML
    private TextField codeBattery;
    @FXML
    private TextField countCycle;
    @FXML
    private TextField conditionBattery;
    @FXML
    private TextField groupBattery;
    @FXML
    private TextField status;
    @FXML
    private ProgressBar progressBar;
    @Setter
    private BatteryFXDTO batteryFXDTO;
    @Getter
    private boolean okClicked = false;
    private static final Logger logger = LogManager.getLogger(BatteryUpdateDataController.class);

    @FXML
    protected void initialize() {
        progressBar.setOpacity(0);
    }

    @SneakyThrows
    @FXML
    private void handleOk() throws SQLException {
            okClicked = true;
            Battery battery = FactoryDAO.getInstance().getBatteryDAO().findEntityById(batteryFXDTO.getId().getValue());
            if (!codeBattery.getText().isEmpty() && isInputField_A()) {
                battery.setCode(codeBattery.getText());
                FactoryDAO.getInstance().getBatteryDAO().updateBatteryByCode(codeBattery.getText(), battery.getId());
                AlertSucceeded.alertSucceeded("Данные аккумулятора " + battery.getId() + " изменены");
                logger.info("Данные аккумулятора " + battery.getId() + " изменены");
            }
            else if (!countCycle.getText().isEmpty() && isInputField_B()) {
                battery.setCycles(Integer.valueOf(countCycle.getText()));
                FactoryDAO.getInstance()
                        .getBatteryDAO()
                        .updateBatteryByCycle(Integer.parseInt(countCycle.getText()), battery.getId());

                AlertSucceeded.alertSucceeded("Данные аккумулятора " + battery.getId() + " изменены");
                logger.info("Данные аккумулятора " + battery.getId() + " изменены");
            }
            else if (!conditionBattery.getCharacters().isEmpty() && isInputField_C()) {
                battery.setCondition(conditionBattery.getText());
                FactoryDAO.getInstance().getBatteryDAO()
                        .updateBatteryByCondition(conditionBattery.getText(), battery.getId());
                AlertSucceeded.alertSucceeded("Данные аккумулятора " + battery.getId() + " изменены");
                logger.info("Данные аккумулятора " + battery.getId() + " изменены");
            }
            else if (!groupBattery.getCharacters().isEmpty() && isInputField_D()) {
                battery.setGroupBattery(groupBattery.getText());
                FactoryDAO.getInstance().getBatteryDAO()
                        .updateBatteryByGroup(groupBattery.getText(), battery.getId());
                AlertSucceeded.alertSucceeded("Данные аккумулятора " + battery.getId() + " изменены");
                logger.info("Данные аккумулятора " + battery.getId() + " изменены");
            }
            else if (!status.getCharacters().isEmpty() && isInputField_E()) {
                battery.setStatus(status.getText());
                FactoryDAO.getInstance()
                        .getBatteryDAO().update(battery);
                AlertSucceeded.alertSucceeded("Данные аккумулятора " + battery.getId() + " изменены");
                logger.info("Данные аккумулятора " + battery.getId() + " изменены");
            } else {
                battery.setCycles(Integer.valueOf(countCycle.getText()));
                battery.setCode(codeBattery.getText());
                battery.setCondition(conditionBattery.getText());
                battery.setGroupBattery(groupBattery.getText());
                battery.setStatus(status.getText());
                FactoryDAO.getInstance().getBatteryDAO().updateBattery(battery);
                AlertSucceeded.alertSucceeded("Данные аккумулятора " + battery.getId() + " изменены");
                logger.info("Данные аккумулятора " + battery.getId() + " изменены");
            }

            codeBattery.clear();
            countCycle.clear();
            conditionBattery.clear();
            groupBattery.clear();
            status.clear();
    }

    private boolean isInputField_A(){
       return  (countCycle.getCharacters().isEmpty() &&
                conditionBattery.getCharacters().isEmpty() &&
                groupBattery.getCharacters().isEmpty() && status.getCharacters().isEmpty());
    }

    private boolean isInputField_B(){
        return  (codeBattery.getCharacters().isEmpty() &&
                conditionBattery.getCharacters().isEmpty() &&
                groupBattery.getCharacters().isEmpty() && status.getCharacters().isEmpty());
    }

    private boolean isInputField_C(){
        return  (codeBattery.getCharacters().isEmpty() &&
                countCycle.getCharacters().isEmpty() &&
                groupBattery.getCharacters().isEmpty() && status.getCharacters().isEmpty());
    }

    private boolean isInputField_D(){
        return  (codeBattery.getCharacters().isEmpty() &&
                countCycle.getCharacters().isEmpty() &&
                conditionBattery.getCharacters().isEmpty() && status.getCharacters().isEmpty());
    }

    private boolean isInputField_E(){
        return  (codeBattery.getCharacters().isEmpty() &&
                countCycle.getCharacters().isEmpty() &&
                conditionBattery.getCharacters().isEmpty() && groupBattery.getCharacters().isEmpty());
    }
}
