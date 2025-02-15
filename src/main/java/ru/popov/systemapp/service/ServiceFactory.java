package ru.popov.systemapp.service;


import ru.popov.systemapp.service.impl.BatteryServiceImpl;
import ru.popov.systemapp.service.impl.EmployeesServiceImpl;
import ru.popov.systemapp.service.impl.JournalServiceImpl;
import ru.popov.systemapp.service.impl.RadiostationServiceImpl;

public class ServiceFactory {
    private static ServiceFactory instance = null;
    private static BatteryService batteryService = null;
    private static EmployeesService employeesService = null;
    private static JournalService journalService = null;
    private static RadiostationService radiostationService = null;

    public static synchronized ServiceFactory getInstance(){
        if (instance == null){
            instance = new ServiceFactory();
        }
        return instance;
    }

    public BatteryService getBatteryService(){
        if (batteryService == null){
            batteryService = new BatteryServiceImpl();
        }
        return batteryService;
    }

    public EmployeesService getEmployeesService(){
        if (employeesService == null){
            employeesService = new EmployeesServiceImpl();
        }
        return employeesService;
    }

    public JournalService getJournalService(){
        if (journalService == null){
            journalService = new JournalServiceImpl();
        }
        return journalService;
    }

    public RadiostationService getRadiostationService(){
        if (radiostationService == null){
            radiostationService = new RadiostationServiceImpl();
        }
        return radiostationService;
    }
}
