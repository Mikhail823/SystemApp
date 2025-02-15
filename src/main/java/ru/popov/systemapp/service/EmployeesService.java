package ru.popov.systemapp.service;


import ru.popov.systemapp.model.Employees;

import java.util.List;

public interface EmployeesService {
    List<Employees> findEmployeesAll();
    List<String> getAllNameEmployees();
    Employees findEmployeeByName(String name);
    void saveEmployee(Employees employees);
}
