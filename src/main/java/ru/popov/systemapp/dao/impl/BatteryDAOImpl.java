package ru.popov.systemapp.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.popov.systemapp.MainApp;
import ru.popov.systemapp.dao.BatteryDAO;
import ru.popov.systemapp.dao.SQLCommandUtil;
import ru.popov.systemapp.dialog.AlertSQLException;
import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Battery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BatteryDAOImpl implements BatteryDAO {

    private static final Logger logger = LogManager.getLogger(BatteryDAOImpl.class);

    public List<Battery> findAll() throws DaoException {
        List<Battery> list = new ArrayList<>();
        try(var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.FIND_ALL_BATTERY)){
            ResultSet resultSet = pst.executeQuery();
            list.addAll(getListBattery(resultSet));
            logger.info("Получен список АКБ в размере " + list.size() + " шт.");
        }catch (SQLException e) {
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
        return list;
    }

    @Override
    public Battery findEntityById(Integer id) throws DaoException, SQLException {
        ResultSet resultSet = null;
        Statement statement = MainApp.getConnection().createStatement();
        resultSet = statement.executeQuery(SQLCommandUtil.FIND_BATTERY_BY_ID + id + ";");
        logger.info("Получение аккумулятора по id " + id);
        return receivingData(resultSet);
    }

    @Override
    public void delete(Battery battery) throws DaoException {

    }

    @Override
    public void create(Battery battery) throws DaoException {

    }

    @Override
    public void update(Battery battery) throws DaoException {

        try{
            var preparedStatement = MainApp.getConnection().prepareStatement(SQLCommandUtil.UPDATE_BATTERY_BY_STATUS);
            preparedStatement.setString(1, battery.getStatus());
            preparedStatement.setInt(2, battery.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            logger.info("Обновление АКБ " + battery.getCode() +" успешное");
        }catch (SQLException e){
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    @Override
    public Battery findBatteryByCode(String codeBattery) throws SQLException {
        var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.FIND_BATTERY_BY_CODE);
        pst.setObject(1, codeBattery);
        logger.info("Получение АКБ по коду " + codeBattery);
        return receivingData(pst.executeQuery());
    }

    @Override
    public List<Battery> findBatteryByDate(LocalDate localDate) {
        List<Battery> batteryList = new ArrayList<>();

        try (var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.FIND_BATTERY_BY_DATE)){
            pst.setObject(1, localDate);
            batteryList.addAll(getListBattery(pst.executeQuery()));

        } catch (SQLException e) {
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
        return batteryList;
    }

    @Override
    public List<Battery> findBatteryByGroup(String group) {
        List<Battery> list = new ArrayList<>();
        try(var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.FIND_BATTERY_BY_GROUP)){
            pst.setObject(1, group);
            list.addAll(getListBattery(pst.executeQuery()));

        } catch (SQLException e) {
            AlertSQLException.sqlException(e.getMessage());
        }

        return list;
    }

    @Override
    public List<Battery> findBatteryByStatus(String status) {
        List<Battery> list = new ArrayList<>();
        try(var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.FIND_BATTERY_BY_STATUS)){
            pst.setObject(1, status);
            list.addAll(getListBattery(pst.executeQuery()));

        } catch (SQLException e) {
            AlertSQLException.sqlException(e.getMessage());
        }

        return list;
    }

    @Override
    public void updateBatteryByCode(String code, Integer id) {
        try (var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.UPDATE_BATTERY_BY_CODE)){
            pst.setObject(1, code);
            pst.setObject(2, id);
            pst.executeUpdate();
        } catch (SQLException e){
            AlertSQLException.sqlException(e.getMessage());
        }
    }

    @Override
    public void updateBatteryByCycle(Integer cycle, Integer id) {
        try (var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.UPDATE_BATTERY_BY_CYCLE)){
            pst.setObject(1, cycle);
            pst.setObject(2, id);
            pst.executeUpdate();
            logger.info("Изменено количество цилов у АКБ " + id + " на " + cycle);
        } catch (SQLException e){
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    @Override
    public void updateBatteryByCondition(String condition, Integer id) {
        try(var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.UPDATE_BATTERY_BY_CONDITION)){
            pst.setObject(1, condition);
            pst.setObject(2, id);
            pst.executeUpdate();
            logger.info("Состояние АКБ " + id + " изменено на " + condition);
        } catch (SQLException e){
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    @Override
    public void updateBatteryByGroup(String group, Integer id) {
        try(var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.UPDATE_BATTERY_BY_GROUP)){
            pst.setObject(1, group);
            pst.setObject(2, id);
            pst.executeUpdate();
        } catch (SQLException e){
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    @Override
    public void updateBattery(Battery battery) throws SQLException {
       try (var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.UPDATE_BATTERY)){
           pst.setObject(1, battery.getCycles());
           pst.setObject(2, battery.getCode());
           pst.setObject(3, battery.getCondition());
           pst.setObject(4, battery.getGroupBattery());
           pst.setObject(5, battery.getStatus());
           pst.setObject(6, battery.getId());
           pst.executeUpdate();
           logger.info("Обновление АКБ " + battery.getId() + " прошло успешно.");
       } catch (SQLException e){
           AlertSQLException.sqlException(e.getMessage());
           logger.error(e.getMessage());
       }
    }

    @Override
    public void updateBatteryChange(Battery battery) {
        try(var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.UPDATE_BATTERY_CHANGE)){
            pst.setObject(1, battery.getCycles());
            pst.setObject(2, battery.getChangeCount());
            pst.setObject(3, battery.getStatus());
            pst.setObject(4, battery.getLastDate());
            pst.setObject(5, battery.getId());
            pst.executeUpdate();
            logger.info("Обновление АКБ " + battery.getId() + " количества заряда.");
        } catch (SQLException e){
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    @Override
    public void updateBatteryDisChange(Battery battery) {
        try(var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.UPDATE_BATTERY_DISCHANGE)){
            pst.setObject(1, battery.getCycles());
            pst.setObject(2, battery.getStatus());
            pst.setObject(3, battery.getDisChangeCount());
            pst.setObject(4, battery.getId());
            pst.executeUpdate();
            logger.info("Обновление АКБ " + battery.getId() + " количества разряда.");
        } catch (SQLException e){
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    private Battery receivingData(ResultSet resultSet) throws SQLException {
        Battery battery = new Battery();
        while (resultSet.next()){
            Integer id = resultSet.getInt("id");
            Integer changeCount = resultSet.getInt("changecount");
            Integer cycles = resultSet.getInt("cycles");
            Integer disChangeCount = resultSet.getInt("dischangecount");
            LocalDate lastDate = resultSet.getObject("lastdate", LocalDate.class);
            String code = resultSet.getString("code");
            String condition = resultSet.getString("condition");
            String descr = resultSet.getString("descr");
            String groupBattery = resultSet.getString("groupbattery");
            String status = resultSet.getString("status_battery");

            battery.setId(id);
            battery.setCode(code);
            battery.setGroupBattery(groupBattery);
            battery.setChangeCount(changeCount);
            battery.setCycles(cycles);
            battery.setDisChangeCount(disChangeCount);
            battery.setLastDate(lastDate);
            battery.setCondition(condition);
            battery.setDescr(descr);
            battery.setStatus(status);
        }
        return battery;
    }

    private List<Battery> getListBattery(ResultSet resultSet) throws SQLException {
        List<Battery> list = new ArrayList<>();

        while (resultSet.next()){

            Integer id = resultSet.getInt("id");
            Integer changeCount = resultSet.getInt("changecount");
            Integer cycles = resultSet.getInt("cycles");
            Integer disChangeCount = resultSet.getInt("dischangecount");
            LocalDate lastDate = resultSet.getObject("lastdate", LocalDate.class);
            String code = resultSet.getString("code");
            String condition = resultSet.getString("condition");
            String descr = resultSet.getString("descr");
            String groupBattery = resultSet.getString("groupbattery");
            String status = resultSet.getString("status_battery");

            list.add(new Battery(id, changeCount, cycles,
                    disChangeCount, lastDate, code,
                    condition, descr, groupBattery, status));
        }
        return list;
    }
}
