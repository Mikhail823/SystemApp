package ru.popov.systemapp.service;


import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import ru.popov.systemapp.dto.BatteryFXDTO;
import ru.popov.systemapp.model.Battery;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface BatteryService {
    List<Battery> findBatteryAll();
    Battery findBatteryByCode(String code) throws SQLException;
    List<Battery> findBatteryByDate(LocalDate date);
    List<Battery> findBatteryByGroup(String group);
    List<Battery> findBatteryByStatus(String status);
}
