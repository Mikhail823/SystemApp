package ru.popov.systemapp.dao;

import java.time.LocalDate;

public class SQLCommandUtil {
    public static String FIND_ALL_JOURNAL = "SELECT * FROM journal;";
    public static String SAVE_JOURNAL = "INSERT INTO journal (name, zav_number, datereceipt, br_number, " +
            "organization, equipment, chexol, changer, battery) VALUES (?,?,?,?,?,?,?,?,?);";
    public static String DELETE_JOURNAL_DATA_BY_ID = "DELETE FROM journal WHERE id = ?;";
    public static String FIND_JOURNAL_DATA_BY_DATE = "SELECT * FROM journal WHERE dateReceipt = ?;";
    public static String FIND_JOURNAL_DATA_BY_NAME = "SELECT *  FROM journal WHERE name = ";
    public static String FIND_JOURNAL_DATA_BY_NAME_AND_DATE = "SELECT * FROM journal WHERE ";
    public static String FIND_JOURNAL_DATA_BY_DATE_LESS_NOW = "SELECT * FROM journal WHERE datereceipt < ?;";
    public static String FIND_JOURNAL_BY_ID = "SELECT * FROM journal WHERE id = ";
    public static String UPDATE_JOURNAL = "UPDATE journal SET battery = ? WHERE id = ?;";

    public  static String FIND_ALL_EMPLOYEES_ORDER_BY_NAME = "SELECT name_empolee FROM employees ORDER BY name_empolee ASC;";
    public static String FIND_EMPLOYEE_BY_NAME = "SELECT * FROM employees WHERE name_empolee = ";
    public static String SAVE_NEW_EMPLOYEE = "INSERT INTO employees (name_empolee, organ) VALUES (?, ?);";

    public static String FIND_BATTERY_BY_CODE = "SELECT * FROM battery WHERE code = ?";
    public static String UPDATE_BATTERY_BY_STATUS = "UPDATE battery SET status_battery = ? WHERE id = ?";
    public static String FIND_BATTERY_BY_DATE = "SELECT * FROM battery WHERE lastdate = ?;";
    public static String FIND_BATTERY_BY_GROUP = "SELECT * FROM battery WHERE groupbattery = ?;";
    public static String FIND_BATTERY_BY_STATUS = "SELECT * FROM battery WHERE status_battery = ?;";
    public static String FIND_BATTERY_BY_ID = "SELECT * FROM battery WHERE id = ";
    public static String UPDATE_BATTERY_BY_CODE = "UPDATE battery SET code = ? WHERE id = ?;";
    public static String UPDATE_BATTERY_BY_CYCLE = "UPDATE battery SET cycles = ? WHERE id = ?;";
    public static String UPDATE_BATTERY_BY_CONDITION = "UPDATE battery SET condition = ? WHERE id = ?;";
    public static String UPDATE_BATTERY_BY_GROUP = "UPDATE battery SET groupbattery = ? WHERE id = ?;";
    public static String UPDATE_BATTERY = "UPDATE battery SET cycles = ?, code = ?, condition = ?, groupbattery = ?, status_battery = ? WHERE id = ?;";
    public static String UPDATE_BATTERY_CHANGE = "UPDATE battery SET cycles = ?, changecount = ?, status_battery = ?, lastdate = ? WHERE id = ?;";
    public static String UPDATE_BATTERY_DISCHANGE = "UPDATE battery SET cycles = ?, status_battery = ?, dischangecount= ? WHERE id = ?;";
    public static String FIND_ALL_BATTERY = "SELECT * FROM battery;";

    public static String FIND_RADIOSTATION_BY_NUMBER = "SELECT * FROM radiostation WHERE zav_number = ";






}
