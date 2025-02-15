package ru.popov.systemapp.dao;


import ru.popov.systemapp.dao.impl.BatteryDAOImpl;
import ru.popov.systemapp.dao.impl.EmployeesDAOImpl;
import ru.popov.systemapp.dao.impl.JournalDAOImpl;
import ru.popov.systemapp.dao.impl.RadiostationDAOImpl;

public class FactoryDAO {

    private static BatteryDAO batteryDAO = null;
    private static EmployeesDAO employeeDAO = null;
    private static RadiostationDAO radiostationDAO = null;
    private static FactoryDAO instance = null;
    private static JournalDAO journalDAO = null;

    public static synchronized FactoryDAO getInstance(){
        if (instance == null){
            instance = new FactoryDAO();
        }
        return instance;
    }

    public BatteryDAO getBatteryDAO(){
        if (batteryDAO == null){
            batteryDAO= new BatteryDAOImpl();
        }
        return batteryDAO;
    }

    public EmployeesDAO getEmployeeDAO(){
        if (employeeDAO == null){
            employeeDAO = new EmployeesDAOImpl();
        }
        return employeeDAO;
    }

    public RadiostationDAO getRadioStationDAO(){
        if (radiostationDAO == null){
            radiostationDAO = new RadiostationDAOImpl();
        }
        return radiostationDAO;
    }

    public JournalDAO getJournalDAO(){
        if (journalDAO == null){
            journalDAO = new JournalDAOImpl();
        }
        return journalDAO;
    }
}
