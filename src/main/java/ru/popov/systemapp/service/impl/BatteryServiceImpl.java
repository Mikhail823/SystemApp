package ru.popov.systemapp.service.impl;


import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.dto.BatteryFXDTO;
import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Battery;
import ru.popov.systemapp.service.BatteryService;
import ru.popov.systemapp.util.LimitCycleBattery;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static ru.popov.systemapp.util.LimitCycleBattery.*;


public class BatteryServiceImpl implements BatteryService {

    @Override
    public List<Battery> findBatteryAll() {
        try {
            return FactoryDAO.getInstance().getBatteryDAO().findAll();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Battery findBatteryByCode(String code) throws SQLException {
        return FactoryDAO.getInstance().getBatteryDAO().findBatteryByCode(code);
    }

    @Override
    public List<Battery> findBatteryByDate(LocalDate date) {
        return FactoryDAO.getInstance().getBatteryDAO().findBatteryByDate(date);
    }

    @Override
    public List<Battery> findBatteryByGroup(String group) {
        return FactoryDAO.getInstance().getBatteryDAO().findBatteryByGroup(group);
    }

    @Override
    public List<Battery> findBatteryByStatus(String status) {
        return FactoryDAO.getInstance().getBatteryDAO().findBatteryByStatus(status);
    }

}
