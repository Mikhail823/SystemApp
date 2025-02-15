package ru.popov.systemapp.dao.impl;

import ru.popov.systemapp.MainApp;
import ru.popov.systemapp.dao.RadiostationDAO;
import ru.popov.systemapp.dao.SQLCommandUtil;
import ru.popov.systemapp.dialog.AlertSQLException;
import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Battery;
import ru.popov.systemapp.model.Radiostation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RadiostationDAOImpl implements RadiostationDAO {
    @Override
    public List<Radiostation> findAll() throws DaoException {
        return List.of();
    }

    @Override
    public Radiostation findEntityById(Integer id) throws DaoException {
        return null;
    }

    @Override
    public void delete(Radiostation radiostation) throws DaoException {

    }

    @Override
    public void create(Radiostation radiostation) throws DaoException {

    }

    @Override
    public void update(Radiostation radiostation) throws DaoException {

    }

    @Override
    public Radiostation findRadioStationByNumber(String number) {
        Radiostation radiostation = new Radiostation();
        try (Statement  statement = MainApp.getConnection().createStatement()){
            ResultSet resultSet = statement.executeQuery(SQLCommandUtil.FIND_RADIOSTATION_BY_NUMBER + "'"+number +"'"+ ";");
            while (resultSet.next()){
                Integer id = resultSet.getInt("id");
                String brNumber = resultSet.getString("br_number");
                String zavNumber = resultSet.getString("zav_number");
                String indexRadio = resultSet.getString("index_radio");
                String garnitura = resultSet.getString("garnitura");
                Boolean isChexol = resultSet.getBoolean("chexol");

                radiostation.setId(id);
                radiostation.setBrNumber(brNumber);
                radiostation.setZavNumber(zavNumber);
                radiostation.setIndexRadio(indexRadio);
                radiostation.setGarnitura(garnitura);
                radiostation.setChexol(isChexol);
            }
        } catch (SQLException e) {
            AlertSQLException.sqlException(e.getMessage());
        }
        return radiostation;
    }
}
