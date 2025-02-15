package ru.popov.systemapp.controller;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.dto.ScanDTO;
import ru.popov.systemapp.model.Battery;
import ru.popov.systemapp.util.OperationAction;

import java.sql.SQLException;
import java.time.LocalDate;

public class QRScannerController {

    @FXML
    private Label infoLabel;
    @FXML
    private Label infoScan;
    @FXML
    private TextArea inputText;
    @FXML
    private ProgressIndicator progress;

    private static final Logger logger = LogManager.getLogger(QRScannerController.class);

    @FXML
    protected void initialize() {
        inputText.setFocusTraversable(true);
        inputText.requestFocus();
    }

    @FXML
    private void onTypedKeyAction(KeyEvent keyEvent) {
        if (keyEvent.getEventType().equals(KeyEvent.KEY_TYPED)) {
            infoScan.setText(getData(inputText));
        }
    }

    @FXML
    private void onClickBtnChangeBattery() throws SQLException {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        for (String code : splitTextInput(inputText)) {
                            Battery battery = FactoryDAO.getInstance().getBatteryDAO().findBatteryByCode(code);
                            battery.setLastDate(LocalDate.now());
                            battery.setChangeCount(battery.getChangeCount() + 1);
                            battery.setStatus(OperationAction.ON_A_CHANGE.toString());
                            if (battery.getChangeCount() > battery.getDisChangeCount()) {
                                battery.setCycles(battery.getDisChangeCount());
                            } else if (battery.getChangeCount() < battery.getDisChangeCount()) {
                                battery.setCycles(battery.getDisChangeCount());
                            } else {
                                battery.setCycles(battery.getCycles() + 1);
                            }
                            FactoryDAO.getInstance().getBatteryDAO().updateBatteryChange(battery);
                        }
                        for (int i = 0; i < 100; i++) {
                            updateProgress(i, 100);
                        }
                        Platform.runLater(() -> {
                            PauseTransition pause = new PauseTransition(Duration.millis(200.0));
                            pause.setOnFinished(e -> {
                                progress.progressProperty().unbind();
                                progress.visibleProperty().unbind();
                                infoLabel.textProperty().unbind();
                            });
                            pause.play();
                        });
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        updateMessage("Аккумуляторы поставлены на заряд");
                        logger.info("Аккумуляторы поставлены на заряд");
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        updateMessage("Произошла ошибка!!!");
                        logger.error("Произошла ошибка!!!");
                    }
                };
            }
        };

        infoLabel.setStyle("-fx-text-fill: black;");

        progress.progressProperty().bind(service.progressProperty());
        progress.visibleProperty().bind(service.runningProperty());
        infoLabel.textProperty().bind(service.messageProperty());

        if (service.getState() == Worker.State.READY) {
            service.start();
        } else {
            service.restart();
        }
    }

    @FXML
    private void onClickBtnDisChangeBattery(){
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        for (String code : splitTextInput(inputText)) {
                            Battery battery = FactoryDAO.getInstance().getBatteryDAO().findBatteryByCode(code);
                            battery.setChangeCount(battery.getChangeCount() + 1);
                            battery.setStatus(OperationAction.ON_DISCHANGE.toString());
                            if (battery.getChangeCount() > battery.getDisChangeCount()) {
                                battery.setCycles(battery.getDisChangeCount());
                            } else if (battery.getChangeCount() < battery.getDisChangeCount()) {
                                battery.setCycles(battery.getDisChangeCount());
                            } else {
                                battery.setCycles(battery.getCycles() + 1);
                            }
                            FactoryDAO.getInstance().getBatteryDAO().updateBatteryDisChange(battery);
                        }
                        for (int i = 0; i < 100; i++) {
                            updateProgress(i, 100);
                        }
                        Platform.runLater(() -> {
                            PauseTransition pause = new PauseTransition(Duration.millis(200.0));
                            pause.setOnFinished(e -> {
                                progress.progressProperty().unbind();
                                progress.visibleProperty().unbind();
                                infoLabel.textProperty().unbind();
                            });
                            pause.play();
                        });
                        return null;
                    }

                    @Override
                    protected void succeeded() {
                        super.succeeded();
                        updateMessage("Аккумуляторы поставлены на разряд");
                        logger.info("Аккумуляторы поставлены на разряд");
                    }

                    @Override
                    protected void failed() {
                        super.failed();
                        updateMessage("Произошла ошибка!!!");
                        logger.error("Произошла ошибка!!!");
                    }
                };
            }
        };

        infoLabel.setStyle("-fx-text-fill: black;");

        progress.progressProperty().bind(service.progressProperty());
        progress.visibleProperty().bind(service.runningProperty());
        infoLabel.textProperty().bind(service.messageProperty());

        if (service.getState() == Worker.State.READY) {
            service.start();
        } else {
            service.restart();
        }
    }

    private String[] splitTextInput(TextArea input) {
        return input.getText().split("\n");
    }

    private void isNotNullTextArea() {
        if (inputText.getText() != null) {
            infoLabel.setStyle("-fx-text-fill: red; -fx-alignment: center");
            infoLabel.setText("Идет сканирование QR-кода....");
        } else {
            infoLabel.getText();
        }
    }

    private String getData(TextArea in) {
        ObservableList<CharSequence> list = in.getParagraphs();
        isNotNullTextArea();
        ScanDTO view = ScanDTO.builder()
                .descr("Всего отсканировано: ")
                .size(list.size() - 1)
                .build();

        return view.toString();

    }

}
