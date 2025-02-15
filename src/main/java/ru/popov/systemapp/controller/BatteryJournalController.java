package ru.popov.systemapp.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.bootstrapfx.BootstrapFX;
import ru.popov.systemapp.MainApp;
import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.dialog.AlertException;
import ru.popov.systemapp.dto.BatteryFXDTO;
import ru.popov.systemapp.model.Battery;
import ru.popov.systemapp.service.OpenScene;
import ru.popov.systemapp.service.ServiceFactory;
import ru.popov.systemapp.util.Group;
import ru.popov.systemapp.util.LimitCycleBattery;
import ru.popov.systemapp.util.OperationAction;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BatteryJournalController {

    @FXML
    private TableView<BatteryFXDTO> batteryTable;
    @FXML
    private TableColumn<BatteryFXDTO, Integer> numberColumn;
    @FXML
    private TableColumn<BatteryFXDTO, String> codeColumn;
    @FXML
    private TableColumn<BatteryFXDTO, LocalDate> lastDateColumn;
    @FXML
    private TableColumn<BatteryFXDTO, Integer> countCiclColumn;
    @FXML
    private TableColumn<BatteryFXDTO, String> conditionColumn;
    @FXML
    private TableColumn<BatteryFXDTO, String> groupColumn;
    @FXML
    private TableColumn<BatteryFXDTO, String> statusColumn;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> selectGroupBox;
    @FXML
    private ComboBox<String> selectCondition;
    @FXML
    private TextField selectCode;
    @FXML
    private Label infoLabel;
    @FXML
    private Label infoCycle;
    @FXML
    private Button btnUpdateBattery;
    @FXML
    private ProgressIndicator progressIndicator;

    private static final Logger logger = LogManager.getLogger(BatteryJournalController.class);

    @FXML
    protected void initialize() {
        progressIndicator.setOpacity(0);
        setStyleTableCell();
        updateBatteryTable(getAllBatteryTable());
        getGroupBox();
        getSelectedStatusBox();

        btnUpdateBattery.setOnAction(e -> {
            BatteryFXDTO batteryFXDTO = batteryTable.getSelectionModel().getSelectedItem();
            onClickBtnDataUpdate(batteryFXDTO);
        });

        checkingTheNumberOfCycles();

    }

    /**
     * TODO
     * Метод для изменения данный выбранного АКБ
     *
     * @param batteryFXDTO
     * @return
     */

    private boolean onClickBtnDataUpdate(BatteryFXDTO batteryFXDTO) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Изменение данных АКБ");
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/updateBattery.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(MainApp.class.getResource("style/updateBattery-view-style.css").toExternalForm());
            stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("img/data.ico")));
            stage.setResizable(false);
            stage.setOnCloseRequest(e -> {
                Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                        "Вы действительно хотите закрыть окно?", ButtonType.NO, ButtonType.OK);
                dialog.setTitle("Закрытие окна");
                dialog.setHeaderText(null);
                dialog.initModality(Modality.WINDOW_MODAL);
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    checkingTheNumberOfCycles();
                    stage.getOnCloseRequest();
                    updateBatteryTable(getAllBatteryTable());

                } else {
                    e.consume();
                }
            });
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(scene);
            stage.show();
            BatteryUpdateDataController controller = fxmlLoader.getController();
            controller.setBatteryFXDTO(batteryFXDTO);
            return controller.isOkClicked();
        } catch (IOException e) {
            AlertException.alertException(e.getMessage());
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * TODO
     * Метод получения всех АКБ для таблицы
     *
     * @return
     */
    private ObservableList<BatteryFXDTO> getAllBatteryTable() {
        ObservableList<BatteryFXDTO> batteryList = FXCollections.observableArrayList();
        Service<ObservableList<BatteryFXDTO>> service = new Service<ObservableList<BatteryFXDTO>>() {
            @Override
            protected Task<ObservableList<BatteryFXDTO>> createTask() {
                return new Task<ObservableList<BatteryFXDTO>>() {
                    @Override
                    protected ObservableList<BatteryFXDTO> call() throws Exception {
                        List<Battery> list = FactoryDAO.getInstance().getBatteryDAO().findAll();
                        for (Battery bat : list) {
                            BatteryFXDTO batteryFXDTO = new BatteryFXDTO(bat.getId(),
                                    bat.getChangeCount(),
                                    bat.getCycles(),
                                    bat.getDisChangeCount(),
                                    bat.getLastDate(),
                                    bat.getCode(),
                                    bat.getCondition(),
                                    bat.getDescr(),
                                    bat.getGroupBattery(),
                                    bat.getStatus());
                            batteryList.add(batteryFXDTO);
                        }
                        logger.info("Получен список АКБ из базы данных.");
                        for (int i = 0; i < 101; i++) {
                            updateProgress(i, 100);
                        }

                        Platform.runLater(() -> {
                            PauseTransition pause = new PauseTransition(Duration.millis(200.0));
                            pause.setOnFinished(e -> {
                                progressIndicator.progressProperty().unbind();
                                progressIndicator.visibleProperty().unbind();
                                progressIndicator.setOpacity(1);
                                infoLabel.textProperty().unbind();
                            });
                            pause.play();
                        });
                        return batteryList;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        updateMessage("Данные обновлены");
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        updateMessage("Произошла ошибка обновления данных из БД");
                    }
                };
            }
        };
        progressIndicator.progressProperty().bind(service.progressProperty());
        progressIndicator.visibleProperty().bind(service.runningProperty());
        infoLabel.textProperty().bind(service.messageProperty());

        if (service.getState() == Worker.State.READY) {
            service.start();
        } else {
            service.restart();
        }
        return batteryList;
    }

    @FXML
    private void updateDataTableBatteryClick() throws SQLException {
        ObservableList<BatteryFXDTO> batteryList = FXCollections.observableArrayList();
        if (datePicker.getValue() != null) {
            for (Battery b : ServiceFactory.getInstance().getBatteryService().findBatteryByDate(datePicker.getValue())) {
                BatteryFXDTO batteryFXDTO = new BatteryFXDTO(b.getId(),
                        b.getChangeCount(),
                        b.getCycles(),
                        b.getDisChangeCount(),
                        b.getLastDate(),
                        b.getCode(),
                        b.getCondition(),
                        b.getDescr(),
                        b.getGroupBattery(),
                        b.getStatus());
                batteryList.add(batteryFXDTO);
            }
            datePicker.setValue(null);
            updateBatteryTable(batteryList);
        } else if (selectGroupBox.getValue() != null) {
            for (Battery b : ServiceFactory.getInstance().getBatteryService().findBatteryByGroup(selectGroupBox.getValue())) {
                BatteryFXDTO batteryFXDTO = new BatteryFXDTO(b.getId(),
                        b.getChangeCount(),
                        b.getCycles(),
                        b.getDisChangeCount(),
                        b.getLastDate(),
                        b.getCode(),
                        b.getCondition(),
                        b.getDescr(),
                        b.getGroupBattery(),
                        b.getStatus());

                batteryList.add(batteryFXDTO);
            }
            selectGroupBox.setValue("");
            updateBatteryTable(batteryList);
        } else if (selectCondition.getValue() != null) {
            for (Battery b : ServiceFactory
                    .getInstance().getBatteryService().findBatteryByStatus(selectCondition.getValue())) {
                BatteryFXDTO batteryFXDTO = new BatteryFXDTO(b.getId(),
                        b.getChangeCount(),
                        b.getCycles(),
                        b.getDisChangeCount(),
                        b.getLastDate(),
                        b.getCode(),
                        b.getCondition(),
                        b.getDescr(),
                        b.getGroupBattery(),
                        b.getStatus());
                batteryList.add(batteryFXDTO);
            }
            selectCondition.setValue("");
            updateBatteryTable(batteryList);
        } else if (!selectCode.getText().isEmpty()) {
            Battery b = ServiceFactory.getInstance().getBatteryService().findBatteryByCode(selectCode.getText());
            BatteryFXDTO batteryFXDTO = new BatteryFXDTO(b.getId(),
                    b.getChangeCount(),
                    b.getCycles(),
                    b.getDisChangeCount(),
                    b.getLastDate(),
                    b.getCode(),
                    b.getCondition(),
                    b.getDescr(),
                    b.getGroupBattery(),
                    b.getStatus());
            batteryList.add(batteryFXDTO);

            selectCode.clear();
            updateBatteryTable(batteryList);
        } else {
            updateBatteryTable(getAllBatteryTable());
        }
        logger.info("Обновление таблицы battery");
    }

    @FXML
    private void clickOpenScannerQRView() {
        OpenScene.openScene("view/scannerQR.fxml",
                "Сканирование QR-кода аккумулятора",
                "style/scannerQR-view-style.css",
                "img/scanner.ico");
    }

    private void getGroupBox() {
        ObservableList<String> groupList = FXCollections.observableArrayList();
        groupList.add(Group.A.toString());
        groupList.add(Group.B.toString());
        groupList.add(Group.C.toString());
        groupList.add(Group.D.toString());
        groupList.add(Group.ZIP.toString());
        groupList.add(Group.HP_519.toString());
        groupList.add(Group.KP_824.toString());
        groupList.add(Group.F_519.toString());
        groupList.add(Group.KP_824F.toString());
        groupList.add(Group.HP_419F.toString());
        groupList.add("");

        selectGroupBox.setItems(groupList);
    }

    private void getSelectedStatusBox() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add(OperationAction.ON_DISCHANGE.toString());
        list.add(OperationAction.ON_THE_ISSUE.toString());
        list.add(OperationAction.ON_A_CHANGE.toString());
        list.add(OperationAction.IN_STOCK.toString());
        list.add("");
        selectCondition.setItems(list);
    }

    private void updateBatteryTable(ObservableList<BatteryFXDTO> list) {
        batteryTable.setItems(list);
        numberColumn.setCellValueFactory(bc -> bc.getValue().getId().asObject());
        lastDateColumn.setCellValueFactory(ld -> ld.getValue().getLastDate());
        countCiclColumn.setCellValueFactory(cc -> cc.getValue().getCycles().asObject());
        conditionColumn.setCellValueFactory(cond -> cond.getValue().getCondition());
        groupColumn.setCellValueFactory(gc -> gc.getValue().getGroupBattery());
        statusColumn.setCellValueFactory(sc -> sc.getValue().getStatus());
        codeColumn.setCellValueFactory(code -> code.getValue().getCode());

        checkingTheNumberOfCycles();
    }

    private void setStyleTableCell() {
        numberColumn.getStyleClass().add("table-cell");
        lastDateColumn.getStyleClass().add("table-cell");
        countCiclColumn.getStyleClass().add("table-cell");
        conditionColumn.getStyleClass().add("table-cell");
        groupColumn.getStyleClass().add("table-cell");
        statusColumn.getStyleClass().add("table-cell");
        codeColumn.getStyleClass().add("table-cell");
    }

    private void checkingTheNumberOfCycles(){
        int number = LimitCycleBattery.QUARTER_LIMIT / LimitCycleBattery.BATTERY_LIMIT;

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        countCiclColumn.setCellFactory( c -> new TableCell<BatteryFXDTO, Integer>(){
                            @Override
                            protected void updateItem(Integer cycle, boolean b) {
                                super.updateItem(cycle, b);
                                if (b || cycle == null) {
                                    setText(null);
                                } else {
                                    setText(cycle.toString());
                                    for (int i = 1; i <= number; i++){
                                        Integer c = LimitCycleBattery.BATTERY_LIMIT * i;
                                        if (cycle.equals(c)) {
                                            setStyle("-fx-text-fill: red");
                                            infoCycle.setStyle("-fx-text-fill: red");
                                            infoCycle.setText("Произведите замену группы аккумуляторов на выдаче!!!");
                                        } else {
                                            setTextFill(Color.BLACK);
                                        }
                                    }
                                }
                            }
                        });
                        return null;
                    }
                };
            }
        };
        if (service.getState() == Worker.State.READY) {
            service.start();
        } else {
            service.restart();
        }
    }
}
