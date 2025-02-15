package ru.popov.systemapp.service.impl;

import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Employees;
import ru.popov.systemapp.service.EmployeesService;

import java.util.List;

public class EmployeesServiceImpl implements EmployeesService {
    @Override
    public List findEmployeesAll() {
        try {
            return FactoryDAO.getInstance().getEmployeeDAO().findAll();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List getAllNameEmployees(){
        return FactoryDAO.getInstance().getEmployeeDAO().findAllEmployeesName();
    }

    @Override
    public Employees findEmployeeByName(String name) {
        return FactoryDAO.getInstance().getEmployeeDAO().findEmployeeByName(name);
    }

    @Override
    public void saveEmployee(Employees employees) {
        try {
            FactoryDAO.getInstance().getEmployeeDAO().create(employees);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
}
