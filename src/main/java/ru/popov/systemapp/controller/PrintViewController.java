package ru.popov.systemapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.transform.Scale;
import lombok.Setter;
import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.dto.JournalFXDTO;
import ru.popov.systemapp.model.Journal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrintViewController {

    @FXML
    private ListView<Journal> list;
    @FXML
    private Button btnPrint;
    private ObservableList<Journal> journals = FXCollections.observableArrayList();


    @FXML
    protected void initialize() {

        for (Journal j : getJournalData()){
            journals.add(j);
        }
        list.setItems(journals);

        btnPrint.setOnAction(e -> {
            printData(list);
        });

    }

    private List<Journal> getJournalData(){
        return FactoryDAO.getInstance().getJournalDAO().findJournalByDateLessNow(LocalDate.now());
    }

    private void printData(ListView<?> listView) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            boolean succes = printerJob.showPrintDialog(listView.getScene().getWindow());
            if (succes) {
//                Node tableToPrint = listView;
//                double scale = 0.85;
//                tableToPrint.getTransforms().add(new Scale(scale, scale));
                boolean prented = printerJob.printPage(listView);
                if (prented) {
                    printerJob.endJob();
                }
            }
        }
    }

}
