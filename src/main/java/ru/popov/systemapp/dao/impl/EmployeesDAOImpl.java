package ru.popov.systemapp.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.popov.systemapp.MainApp;
import ru.popov.systemapp.dao.EmployeesDAO;
import ru.popov.systemapp.dao.SQLCommandUtil;
import ru.popov.systemapp.dialog.AlertSQLException;
import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Employees;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeesDAOImpl implements EmployeesDAO {
    private static final Logger logger = LogManager.getLogger(EmployeesDAOImpl.class);
    @Override
    public List<Employees> findAll() throws DaoException {
        return List.of();
    }

    @Override
    public Employees findEntityById(Integer id) throws DaoException {
        return null;
    }

    @Override
    public void delete(Employees employees) throws DaoException {

    }

    @Override
    public void create(Employees employees) throws DaoException {

        try (var pst = MainApp.getConnection().prepareStatement(SQLCommandUtil.SAVE_NEW_EMPLOYEE)){
            pst.setObject(1, employees.getName_empolee());
            pst.setObject(2, employees.getOrgan());
            pst.execute();
            logger.info("Записан в БД новый сотрудник " + employees.getName_empolee());
        } catch (SQLException e) {
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
    }

    @Override
    public void update(Employees employees) throws DaoException {

    }

    @Override
    public List<String> findAllEmployeesName() {
        List<String> employeesList = new ArrayList<>();
        try(Statement statement = MainApp.connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(SQLCommandUtil.FIND_ALL_EMPLOYEES_ORDER_BY_NAME);
            while (resultSet.next()){
                String name_empolee = resultSet.getString("name_empolee");
                employeesList.add(name_empolee);
            }
            logger.info("Получен список имен сотрудников");
        } catch (SQLException exception){
            AlertSQLException.sqlException(exception.getMessage());
            logger.error(exception.getMessage());
        }
        return employeesList;
    }

    @Override
    public Employees findEmployeeByName(String name) {
        Employees employees = new Employees();
        try(Statement  statement  = MainApp.getConnection().createStatement()) {
            ResultSet  resultSet = statement.executeQuery(SQLCommandUtil.FIND_EMPLOYEE_BY_NAME + "'"+name+ "'"+  ";");
            while (resultSet.next()){
                Integer id = resultSet.getInt("id");
                String nameEmployee =  resultSet.getString("name_empolee");
                String organ = resultSet.getString("organ");
                employees.setId(id);
                employees.setName_empolee(nameEmployee);
                employees.setOrgan(organ);
            }
        } catch (SQLException e) {
            AlertSQLException.sqlException(e.getMessage());
            logger.error(e.getMessage());
        }
        return employees;
    }

}
