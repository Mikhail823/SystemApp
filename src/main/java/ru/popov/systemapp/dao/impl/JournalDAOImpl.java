package ru.popov.systemapp.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.popov.systemapp.MainApp;
import ru.popov.systemapp.dao.JournalDAO;
import ru.popov.systemapp.dao.SQLCommandUtil;
import ru.popov.systemapp.dialog.AlertSQLException;
import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Journal;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JournalDAOImpl implements JournalDAO {

    private static final Logger logger = LogManager.getLogger(JournalDAOImpl.class);

    @Override
    public List<Journal> findAll() throws DaoException {
        List<Journal> journalList = new ArrayList<>();
        try (Statement statement = MainApp.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(SQLCommandUtil.FIND_ALL_JOURNAL);
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String zavNumber = resultSet.getString("zav_number");
                LocalDate dateReceipt = resultSet.getObject("datereceipt", LocalDate.class);
                String organization = resultSet.getString("organization");
                String equipment = resultSet.getString("equipment");
                String brNumber = resultSet.getString("br_number");
                String chexol = resultSet.getString("chexol");
                String changer = resultSet.getString("changer");
                String battery = resultSet.getString("battery");

                journalList.add(new Journal(id, name, zavNumber, dateReceipt, organization, equipment, brNumber,
                        chexol, changer, battery));
            }
            logger.info("Получен список электронного журнала");

        } catch (SQLException exception) {
            AlertSQLException.sqlException(exception.getMessage());
            logger.error(exception.getMessage());
        }
        return journalList;
    }

    @Override
    public Journal findEntityById(Integer id) throws SQLException {
        var pst = MainApp.getConnection().createStatement();
        ResultSet resultSet = pst.executeQuery(SQLCommandUtil.FIND_JOURNAL_BY_ID + "'" + id + "';");
        return getJournal(resultSet);
    }

    @Override
    public void delete(Journal journal) throws DaoException {
        try (var preparedStatement = MainApp.getConnection().prepareStatement(SQLCommandUtil.DELETE_JOURNAL_DATA_BY_ID)){
            preparedStatement.setObject(1, journal.getId());
            preparedStatement.execute();
            logger.info("Данные сотрудника удалены " + journal.getName());
        } catch (SQLException e){
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    @Override
    public void create(Journal journal) throws DaoException {
        try (var preparedStatement = MainApp.getConnection().prepareStatement(SQLCommandUtil.SAVE_JOURNAL)) {

            preparedStatement.setObject(1, journal.getName());
            preparedStatement.setObject(2, journal.getZav_number());
            preparedStatement.setObject(3, journal.getDateReceipt());
            preparedStatement.setObject(4, journal.getBr_number());
            preparedStatement.setObject(5, journal.getOrganization());
            preparedStatement.setObject(6, journal.getEquipment());
            preparedStatement.setObject(7, journal.getChexol());
            preparedStatement.setObject(8, journal.getChanger());
            preparedStatement.setObject(9, journal.getBattery());
            preparedStatement.execute();
            logger.info("Произведена новая запись в журнал " + journal.getName());
        } catch (SQLException exception) {
            AlertSQLException.sqlException(exception.getMessage());
            logger.error(exception.getMessage());
        }
    }

    @Override
    public void update(Journal journal) throws DaoException, SQLException {
        var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.UPDATE_JOURNAL);
        pst.setObject(1, journal.getBattery());
        pst.setObject(2, journal.getId());
        pst.executeUpdate();
        logger.info("Данные сотрудника " + journal.getName() + " обновлены.");
        pst.close();
    }

    @Override
    public void deleteById(Integer id) {

        try (var preparedStatement = MainApp.getConnection().prepareStatement(SQLCommandUtil.DELETE_JOURNAL_DATA_BY_ID)) {
            preparedStatement.setObject(1, id);
            preparedStatement.execute();
            logger.info("Удаление данных сотрудника под номером " + id + " произведено успешно.");
        } catch (SQLException exception) {
            AlertSQLException.sqlException(exception.getMessage());
            logger.error(exception.getMessage());
        }
    }

    @Override
    public List<Journal> getAllDataJournalByDate(LocalDate date) throws SQLException {

        var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.FIND_JOURNAL_DATA_BY_DATE);
        pst.setObject(1, date);
        ResultSet resultSet = pst.executeQuery();
        return getListJournal(resultSet);
    }

    @Override
    public Journal findJournalByName(String nameEmployee) throws SQLException {
        Statement statement = MainApp.getConnection().createStatement();
        var rs = statement.executeQuery(SQLCommandUtil.FIND_JOURNAL_DATA_BY_NAME + "'" + nameEmployee + "'"+ ";");
        return getJournal(rs);
    }

    @Override
    public Journal findJournalByNameAndDate(String name, LocalDate date) throws SQLException {
        var pst = MainApp.getConnection().createStatement();
        ResultSet    resulSet = pst.executeQuery(SQLCommandUtil.FIND_JOURNAL_DATA_BY_NAME_AND_DATE + "name = "+ "'" + name + "'" + " AND " + "datereceipt = "+ "'" + date + "';");

        return getJournal(resulSet);
    }

    @Override
    public List<Journal> findJournalByDateLessNow(LocalDate date) {
        List<Journal> list = new ArrayList<>();
        try(var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.FIND_JOURNAL_DATA_BY_DATE_LESS_NOW)){
            pst.setObject(1, date);
            ResultSet resultSet = pst.executeQuery();
            list.addAll(getListJournal(resultSet));
        } catch (SQLException e){
            AlertSQLException.sqlException(e.getMessage());
        }
        return list;
    }

    private Journal getJournal(ResultSet resultSet) throws SQLException {
        Journal journal = new Journal();

        while (resultSet.next()) {
            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String zavNumber = resultSet.getString("zav_number");
            LocalDate dateReceipt = resultSet.getObject("datereceipt", LocalDate.class);
            String organization = resultSet.getString("organization");
            String equipment = resultSet.getString("equipment");
            String brNumber = resultSet.getString("br_number");
            String chexol = resultSet.getString("chexol");
            String changer = resultSet.getString("changer");
            String battery = resultSet.getString("battery");

            journal.setId(id);
            journal.setName(name);
            journal.setZav_number(zavNumber);
            journal.setDateReceipt(dateReceipt);
            journal.setOrganization(organization);
            journal.setEquipment(equipment);
            journal.setBr_number(brNumber);
            journal.setChanger(chexol);
            journal.setChanger(changer);
            journal.setBattery(battery);
        }
        return journal;
    }

    private List<Journal> getListJournal(ResultSet resultSet) throws SQLException {
        List<Journal> journalList = new ArrayList<>();

        while (resultSet.next()){
            Integer id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            String zavNumber = resultSet.getString("zav_number");
            LocalDate dateReceipt = resultSet.getObject("datereceipt", LocalDate.class);
            String organization = resultSet.getString("organization");
            String equipment = resultSet.getString("equipment");
            String brNumber = resultSet.getString("br_number");
            String chexol = resultSet.getString("chexol");
            String changer = resultSet.getString("changer");
            String battery = resultSet.getString("battery");

            journalList.add(new Journal(id, name, zavNumber, dateReceipt, organization, equipment, brNumber,
                    chexol, changer, battery));

        }
        return journalList;
    }
}
