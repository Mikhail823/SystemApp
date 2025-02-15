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
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kordamp.bootstrapfx.BootstrapFX;

import ru.popov.systemapp.MainApp;
import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.dialog.AlertException;
import ru.popov.systemapp.dialog.AlertInfo;
import ru.popov.systemapp.dialog.AlertSucceeded;
import ru.popov.systemapp.dto.EmployeesNameFXDTO;
import ru.popov.systemapp.dto.JournalFXDTO;
import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Battery;
import ru.popov.systemapp.model.Employees;
import ru.popov.systemapp.model.Journal;
import ru.popov.systemapp.model.Radiostation;
import ru.popov.systemapp.service.OpenScene;
import ru.popov.systemapp.service.ServiceFactory;
import ru.popov.systemapp.util.Case;
import ru.popov.systemapp.util.Changer;
import ru.popov.systemapp.util.OperationAction;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {
    @FXML
    private TableView<JournalFXDTO> journalTable;
    @FXML
    private TableColumn<JournalFXDTO, Integer> numberColumn;
    @FXML
    private TableColumn<JournalFXDTO, LocalDate> dateColumn;
    @FXML
    private TableColumn<JournalFXDTO, String> nameColumn;
    @FXML
    private TableColumn<JournalFXDTO, String> zavNumberColumn;
    @FXML
    private TableColumn<JournalFXDTO, String> brNumberColumn;
    @FXML
    private TableColumn<JournalFXDTO, String> caseColumn;
    @FXML
    private TableColumn<JournalFXDTO, String> changerColumn;
    @FXML
    private TableColumn<JournalFXDTO, String> batteryColumn;
    @FXML
    private TableColumn<JournalFXDTO, String> headsetColumn;
    @FXML
    private ChoiceBox<EmployeesNameFXDTO> choiceBoxData;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<EmployeesNameFXDTO> employeeComboBox;
    @FXML
    private TextArea inputText;
    @FXML
    private ComboBox<String> changerComboBox;
    @FXML
    private ProgressBar mainProgressBar;
    @FXML
    private Button btnSave;
    @FXML
    private Label infoLabel;
    @FXML
    private Label numberOfIssued;
    @FXML
    private Label numberDebtors;
    @FXML
    private Button btnPrintMain;
    @FXML
    private Button btnUpdateBattery;
    @FXML
    private Circle circle;
    private static final Logger logger = LogManager.getLogger(MainController.class);

    private AtomicReference<Integer> numberIssued = new AtomicReference<>(0);
    private AtomicReference<Integer> numberOfDebtors = new AtomicReference<>(0);

    @FXML
    protected void initialize() throws DaoException, SQLException {

        addStyleCellTable();
        mainProgressBar.setOpacity(0);
        updateDataTableJournal(getAllDataJournal());

        employeeComboBox.setItems(getEmployeesName());
        choiceBoxData.setItems(getEmployeesName());
        changerComboBox.setItems(getChargerList());
        changingTheBackgroundColorByDate();
        countingTheNumberOfDebtors();

        btnPrintMain.setOnAction(e -> {
            openPrintViewTableJournal();
        });
        openUpdateRadiostationUser();

        if (MainApp.getConnection() != null) {
            circle.setFill(Color.GREEN);
            logger.info("Соединение с БД успешное.");
        } else {
            circle.setFill(Color.RED);
            logger.error("Соединение с БД разорвано.");
        }
    }

    /**
     * TODO
     * Метод замены АКБ
     */

    private void openUpdateRadiostationUser() {

        btnUpdateBattery.setOnAction(e -> {
            JournalFXDTO journalFXDTO = journalTable.getSelectionModel().getSelectedItem();

            try {
                Stage stage = new Stage();
                stage.setTitle("Замена АКБ");
                FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/batteryReplacementView.fxml"));

                Scene scene = new Scene(fxmlLoader.load());
                scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                scene.getStylesheets().add(MainApp.class
                        .getResource("style/batteryReplacement-style.css").toExternalForm());
                stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("img/disChange.ico")));
                stage.setResizable(false);
                stage.setOnCloseRequest(me -> {
                    Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                            "Вы действительно хотите закрыть окно?", ButtonType.NO, ButtonType.OK);
                    dialog.setTitle("Закрытие окна");
                    dialog.setHeaderText(null);
                    dialog.initModality(Modality.WINDOW_MODAL);
                    Optional<ButtonType> result = dialog.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        stage.getOnCloseRequest();
                        try {
                            updateDataTableJournal(getAllDataJournal());
                        } catch (DaoException ex) {
                            AlertException.alertException(ex.getMessage());
                            logger.error(ex.getMessage());
                        }
                    } else {
                        e.consume();
                    }
                });
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setScene(scene);
                stage.show();
                UpdateUserBatteryController controller = fxmlLoader.getController();
                controller.setJournalFXDTO(journalFXDTO);
            } catch (IOException ex) {
                AlertException.alertException(ex.getMessage());
                logger.error(ex.getMessage());
            }
        });
    }

    /**
     * Метод получения имен сотрудников
     *
     * @return
     */
    public ObservableList<EmployeesNameFXDTO> getEmployeesName() {

        ObservableList<EmployeesNameFXDTO> list = FXCollections.observableArrayList();
        Service<ObservableList<EmployeesNameFXDTO>> service = new Service<ObservableList<EmployeesNameFXDTO>>() {
            @Override
            protected Task<ObservableList<EmployeesNameFXDTO>> createTask() {
                return new Task<ObservableList<EmployeesNameFXDTO>>() {
                    @Override
                    protected ObservableList<EmployeesNameFXDTO> call() throws Exception {
                        List<String> employeesList = FactoryDAO.getInstance().getEmployeeDAO().findAllEmployeesName();
                        for (String e : employeesList) {
                            list.add(new EmployeesNameFXDTO(e));
                        }
                        for (int i = 0; i < 100; i++) {
                            updateProgress(i, 100);
                        }
                        Platform.runLater(() -> {
                            PauseTransition pause = new PauseTransition(Duration.millis(200.0));
                            pause.setOnFinished(e -> {
                                mainProgressBar.progressProperty().unbind();
                                mainProgressBar.visibleProperty().unbind();
                                infoLabel.textProperty().unbind();
                            });
                            pause.play();
                        });
                        return list;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        updateMessage("Данные из БД получены");
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        updateMessage("Произошла ошибка получения данных из БД");
                    }
                };
            }
        };

        mainProgressBar.progressProperty().bind(service.progressProperty());
        mainProgressBar.visibleProperty().bind(service.runningProperty());
        infoLabel.textProperty().bind(service.messageProperty());

        if (service.getState() == Worker.State.READY) {
            service.start();
        } else {
            service.restart();
        }
        return list;
    }

    @FXML
    private void onTypedKeyAction(KeyEvent keyEvent) {
        if (keyEvent.getEventType().equals(KeyEvent.KEY_TYPED)) {
            splitInputText(inputText);
        }
    }

    private void openPrintViewTableJournal() {
        OpenScene.openScene("view/printView.fxml",
                "Печать данных",
                "style/printView-style.css",
                "img/printer.ico");
    }

    @FXML
    private void onActionUpdateTableTheUser() throws SQLException, DaoException {
        ObservableList<JournalFXDTO> list = FXCollections.observableArrayList();
        if (datePicker.getValue() == null && choiceBoxData.getValue() != null) {
            Journal journal = FactoryDAO.getInstance().getJournalDAO().findJournalByName(choiceBoxData.getValue().toString());
            if (journal.getId() == null) {
                AlertInfo.info("Данные не найдены!");
                logger.error("Данные журнала в базе не найдены.");
            } else {
                JournalFXDTO journalFXDTO = new JournalFXDTO(journal.getId(), journal.getName(),
                        journal.getZav_number(), journal.getDateReceipt(), journal.getOrganization(),
                        journal.getEquipment(), journal.getBr_number(), journal.getChexol(), journal.getChanger(), journal.getBattery());
                list.add(journalFXDTO);
                updateDataTableJournal(list);
            }
            choiceBoxData.getSelectionModel().clearSelection();
        } else if (datePicker.getValue() != null && choiceBoxData.getValue() == null) {
            var journalList = FactoryDAO.getInstance().getJournalDAO().getAllDataJournalByDate(datePicker.getValue());
            if (journalList.isEmpty()) {
                AlertInfo.info("Данные не найдены!");
                logger.error("Данные журнала в базе не найдены.");
            } else {
                for (Journal journal : journalList) {
                    JournalFXDTO journalFXDTO = new JournalFXDTO(journal.getId(), journal.getName(),
                            journal.getZav_number(), journal.getDateReceipt(), journal.getOrganization(),
                            journal.getEquipment(), journal.getBr_number(), journal.getChexol(), journal.getChanger(), journal.getBattery());
                    list.add(journalFXDTO);
                    updateDataTableJournal(list);
                }
            }
            datePicker.setValue(null);
        } else {
            if (choiceBoxData.getValue() == null && datePicker.getValue() == null) {
                updateDataTableJournal(getAllDataJournal());
            } else {
                var journal = FactoryDAO.getInstance().getJournalDAO()
                        .findJournalByNameAndDate(choiceBoxData.getValue().toString(), datePicker.getValue());
                if (journal.getId() == null) {
                    AlertInfo.info("Данные не найдены!");
                    logger.error("Данные журнала в базе не найдены.");
                } else {
                    JournalFXDTO journalFXDTO = new JournalFXDTO(journal.getId(), journal.getName(),
                            journal.getZav_number(), journal.getDateReceipt(), journal.getOrganization(),
                            journal.getEquipment(), journal.getBr_number(), journal.getChexol(), journal.getChanger(), journal.getBattery());

                    list.add(journalFXDTO);
                    updateDataTableJournal(list);
                    datePicker.setValue(null);
                    choiceBoxData.getSelectionModel().clearSelection();
                }
            }
        }
    }

    /**
     * TODO
     * Метод для записи в журнал выдачи радиостанций
     */
    @FXML
    private void saveJournal() throws DaoException {
        Service<Void> saveService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Employees employees = ServiceFactory
                                .getInstance().getEmployeesService().findEmployeeByName(employeeComboBox.getValue().toString());

                        if (splitInputText(inputText).length > 1) {
                            Radiostation radiostation = ServiceFactory.getInstance().getRadiostationService().findRadioStationByNumber(splitInputText(inputText)[0]);
                            issuingARadioStation(radiostation, employees);

                        } else {
                            if (matcherRegex(splitInputText(inputText)[0]).find()) {
                                issuingOneBattery(employees);
                            }
                        }

                        for (int i = 0; i < 101; i++) {
                            updateProgress(i, 100);
                        }

                        Platform.runLater(() -> {
                            PauseTransition pause = new PauseTransition(Duration.millis(200.0));
                            pause.setOnFinished(e -> {
                                mainProgressBar.progressProperty().unbind();
                                mainProgressBar.visibleProperty().unbind();
                                infoLabel.textProperty().unbind();

                                numberIssued.set(journalTable.getItems().size());
                                Integer count = numberIssued.get();
                                numberOfIssued.setText(String.valueOf(count));

                                employeeComboBox.setValue(null);
                                changerComboBox.setValue(null);
                                inputText.clear();
                            });
                            pause.play();
                        });
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        updateMessage("Запись успешна!!!");
                        AlertSucceeded.alertSucceeded("Молодец записал!!!");
                        try {
                            updateDataTableJournal(getAllDataJournal());
                        } catch (DaoException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        updateMessage("Произошла ошибка записи данных в БД");
                        AlertException.alertException("Возникла ошибка сохранения в БД. Проверьте входные данные.");
                    }
                };
            }
        };

        mainProgressBar.progressProperty().bind(saveService.progressProperty());
        mainProgressBar.visibleProperty().bind(saveService.runningProperty());
        infoLabel.textProperty().bind(saveService.messageProperty());

        if (saveService.getState() == Worker.State.READY) {
            saveService.start();
        } else {
            saveService.restart();
        }
    }

    /**
     * TODO
     * Метод для удаления из журнала
     *
     * @return
     */
    @FXML
    private void onActionWriteOff() {
        Service<Void> serviceWrite = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        deleteSelectedItemJournal();
                        for (int i = 0; i < 100; i++) {
                            updateProgress(i, 100);
                        }
                        Platform.runLater(() -> {
                            PauseTransition pause = new PauseTransition(Duration.millis(200.0));
                            pause.setOnFinished(e -> {
                                mainProgressBar.progressProperty().unbind();
                                mainProgressBar.visibleProperty().unbind();
                                infoLabel.textProperty().unbind();

                                numberIssued.set(journalTable.getItems().size());
                                Integer count = numberIssued.get();
                                numberOfIssued.setText(String.valueOf(count));
                                countingTheNumberOfDebtors();
                            });
                            pause.play();
                        });
                        return null;
                    }

                    @SneakyThrows
                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        updateMessage("Радиостанция списана");
                        logger.info("Данные успешно удалены из базы данных");
                        try {
                            updateDataTableJournal(getAllDataJournal());
                        } catch (DaoException e) {
                            AlertException.alertException(e.getMessage());
                            logger.error(e.getMessage());
                        }
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        updateMessage("Произошла ошибка обновления данных из БД");
                    }
                };
            }
        };

        mainProgressBar.progressProperty().bind(serviceWrite.progressProperty());
        mainProgressBar.visibleProperty().bind(serviceWrite.runningProperty());
        infoLabel.textProperty().bind(serviceWrite.messageProperty());

        if (serviceWrite.getState() == Worker.State.READY) {
            serviceWrite.start();
        } else {
            serviceWrite.restart();
        }
    }

    @FXML
    private void openJournalBatteryView() {
        OpenScene.openScene("view/battery.fxml",
                "Аккумуляторный журнал", "style/batteryJournal-style.css",
                "img/battery.ico");
    }

    /**
     * Todo
     * Метод для сохранения нового сторудника в БД
     */

    @FXML
    private void openSaveNewEmployeeView() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Добавление нового сотрудника");
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/newEmployeee.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setResizable(false);
            scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
            scene.getStylesheets().add(MainApp.class.getResource("style/newEmployee-view-style.css").toExternalForm());
            stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("img/user.ico")));
            stage.setOnCloseRequest(e -> {
                Alert dialog = new Alert(Alert.AlertType.CONFIRMATION,
                        "Вы действительно хотите закрыть окно?", ButtonType.NO, ButtonType.OK);
                dialog.setTitle("Закрытие окна");
                dialog.setHeaderText(null);
                dialog.initModality(Modality.WINDOW_MODAL);
                Optional<ButtonType> result = dialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    stage.getOnCloseRequest();
                    employeeComboBox.setItems(getEmployeesName());
                } else {
                    e.consume();
                }
            });
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            AlertException.alertException(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    private ObservableList<JournalFXDTO> getAllDataJournal() throws DaoException {
        ObservableList<JournalFXDTO> list = FXCollections.observableArrayList();
        List<Journal> journals = FactoryDAO.getInstance().getJournalDAO().findAll();
        for (Journal j : journals) {
            JournalFXDTO journalFXDTO =
                    new JournalFXDTO(j.getId(),
                            j.getName(),
                            j.getZav_number(),
                            j.getDateReceipt(),
                            j.getOrganization(),
                            j.getEquipment(),
                            j.getBr_number(),
                            j.getChexol(),
                            j.getChanger(),
                            j.getBattery());
            list.add(journalFXDTO);
        }

        return list;
    }

    private ObservableList<String> getChargerList() {
        ObservableList<String> listChanger = FXCollections.observableArrayList();
        listChanger.add(Changer.CD.toString());
        listChanger.add(Changer.NB.toString());
        listChanger.add(Changer.NBU.toString());
        listChanger.add(Changer.VAC.toString());
        return listChanger;
    }

    private void updateDataTableJournal(ObservableList<JournalFXDTO> list) {
        journalTable.setItems(list);
        numberColumn.setCellValueFactory(i -> i.getValue().getId().asObject());
        dateColumn.setCellValueFactory(d -> d.getValue().getDateReceipt());
        nameColumn.setCellValueFactory(n -> n.getValue().getName());
        zavNumberColumn.setCellValueFactory(nu -> nu.getValue().getZavNumber());
        brNumberColumn.setCellValueFactory(br -> br.getValue().getBrNumber());
        headsetColumn.setCellValueFactory(ee -> ee.getValue().getEquipment());
        caseColumn.setCellValueFactory(cc -> cc.getValue().getChexol());
        changerColumn.setCellValueFactory(ch -> ch.getValue().getChanger());
        batteryColumn.setCellValueFactory(bat -> bat.getValue().getBattery());

        numberIssued.set(journalTable.getItems().size());
        Integer count = numberIssued.get();
        numberOfIssued.setText(String.valueOf(count));
    }

    private void addStyleCellTable() {
        numberColumn.getStyleClass().add("journal-table-cell");
        dateColumn.getStyleClass().add("journal-table-cell");
        nameColumn.getStyleClass().add("journal-table-cell");
        zavNumberColumn.getStyleClass().add("journal-table-cell");
        brNumberColumn.getStyleClass().add("journal-table-cell");
        headsetColumn.getStyleClass().add("journal-table-cell");
        caseColumn.getStyleClass().add("journal-table-cell");
        changerColumn.getStyleClass().add("journal-table-cell");
        batteryColumn.getStyleClass().add("journal-table-cell");
    }

    private void changingTheBackgroundColorByDate() {

        dateColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean b) {
                super.updateItem(date, b);
                if (b || date == null) {
                    setText(null);
                } else {
                    setText(date.toString());
                    if (date.isBefore(LocalDate.now())) {
                        setTextFill(Color.RED);
                    } else {
                        setTextFill(Color.BLACK);
                    }
                }
            }
        });
    }

    private void countingTheNumberOfDebtors() {
        numberOfDebtors.set(mappingEntityByFX(FactoryDAO.getInstance()
                .getJournalDAO().findJournalByDateLessNow(LocalDate.now())).size());
        numberDebtors.setText(String.valueOf(numberOfDebtors.get()));
    }

    private String[] splitInputText(TextArea inputText) {
        String[] list = inputText.getText().split("\n");
        return list;
    }

    private Matcher matcherRegex(String text) {
        String regex = "^[714]";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(text);
    }

    /**
     * TODO
     * Метод для выдачи одного АКБ
     *
     * @param employees
     * @throws DaoException
     * @throws SQLException
     */

    private void issuingOneBattery(Employees employees) throws DaoException, SQLException {
        Battery battery = ServiceFactory.getInstance().getBatteryService()
                .findBatteryByCode(splitInputText(inputText)[0]);
        if (battery != null) {
            Journal journal =
                    new Journal(employees.getName_empolee(),
                            null,
                            LocalDate.now(),
                            employees.getOrgan(),
                            null,
                            null,
                            null,
                            null,
                            splitInputText(inputText)[0]);
            ServiceFactory.getInstance().getJournalService().save(journal);
            battery.setStatus(OperationAction.ON_THE_ISSUE.toString());
            try {
                FactoryDAO.getInstance().getBatteryDAO().update(battery);
            } catch (DaoException e) {
                throw new RuntimeException(e);
            }
        } else {
            AlertException.alertException("Полученные данные отсутствуют в БД!");
            logger.error("Полученные данные отсутствуют в БД! {0}", battery);
        }
    }

    private void issuingARadioStation(Radiostation radiostation, Employees employees) throws DaoException, SQLException {
        Battery battery = FactoryDAO.getInstance().getBatteryDAO().findBatteryByCode(splitInputText(inputText)[1].toString());
        if (radiostation.getGarnitura() == null) {
            Journal journal = new Journal(employees.getName_empolee(),
                    splitInputText(inputText)[0],
                    LocalDate.now(),
                    employees.getOrgan(),
                    radiostation.getGarnitura(),
                    radiostation.getBrNumber(),
                    Case.WITH_A_CASE.toString(),
                    changerComboBox.getValue(),
                    splitInputText(inputText)[1]);

            FactoryDAO.getInstance().getJournalDAO().create(journal);
        } else {
            Journal journal = new Journal(employees.getName_empolee(),
                    splitInputText(inputText)[0],
                    LocalDate.now(),
                    employees.getOrgan(),
                    radiostation.getGarnitura(),
                    radiostation.getBrNumber(),
                    Case.WITHOUT_A_COVER.toString(),
                    changerComboBox.getValue(),
                    splitInputText(inputText)[1]);
            FactoryDAO.getInstance().getJournalDAO().create(journal);
        }
        battery.setStatus(OperationAction.ON_THE_ISSUE.toString());
        FactoryDAO.getInstance().getBatteryDAO().update(battery);
    }

    private void deleteSelectedItemJournal() throws DaoException, SQLException {
        JournalFXDTO journalFXDTO = journalTable.getSelectionModel().getSelectedItem();
        Battery battery = ServiceFactory.getInstance().getBatteryService()
                .findBatteryByCode(journalFXDTO.getBattery().getValue());
        if (battery != null) {
            journalTable.getItems().remove(journalFXDTO);
            battery.setStatus("");
            FactoryDAO.getInstance().getBatteryDAO().update(battery);
            ServiceFactory.getInstance().getJournalService().deleteById(journalFXDTO.getId().getValue());
        } else {
            AlertException.alertException("Данные АКБ записаны не верно. Удалите из СУБД самостоятельно!");
            logger.error("Данные АКБ записаны не верно. Удалите из СУБД самостоятельно!");
        }
    }

    private ObservableList<JournalFXDTO> mappingEntityByFX(List<Journal> journals) {
        ObservableList<JournalFXDTO> list = FXCollections.observableArrayList();
        for (Journal j : journals) {
            JournalFXDTO journalFXDTO =
                    new JournalFXDTO(j.getId(),
                            j.getName(),
                            j.getZav_number(),
                            j.getDateReceipt(),
                            j.getOrganization(),
                            j.getEquipment(),
                            j.getBr_number(),
                            j.getChexol(),
                            j.getChanger(),
                            j.getBattery());
            list.add(journalFXDTO);
        }
        return list;
    }
}