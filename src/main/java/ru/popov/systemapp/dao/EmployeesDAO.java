package ru.popov.systemapp.dao;

import ru.popov.systemapp.model.Employees;

import java.util.List;

public interface EmployeesDAO extends BaseDAO<Integer, Employees> {
    List<String> findAllEmployeesName();
    Employees findEmployeeByName(String name);
}
