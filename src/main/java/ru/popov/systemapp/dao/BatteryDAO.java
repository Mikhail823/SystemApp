package ru.popov.systemapp.dao;

import ru.popov.systemapp.model.Battery;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface BatteryDAO extends BaseDAO<Integer, Battery> {
    Battery findBatteryByCode(String code) throws SQLException;
    List<Battery> findBatteryByDate(LocalDate localDate);
    List<Battery> findBatteryByGroup(String group);
    List<Battery> findBatteryByStatus(String status);
    void updateBatteryByCode(String code, Integer id);
    void updateBatteryByCycle(Integer cycle, Integer id);
    void updateBatteryByCondition(String condition, Integer id);
    void updateBatteryByGroup(String group, Integer id);
    void  updateBattery(Battery battery) throws SQLException;
    void updateBatteryChange(Battery battery);
    void updateBatteryDisChange(Battery battery);
}
