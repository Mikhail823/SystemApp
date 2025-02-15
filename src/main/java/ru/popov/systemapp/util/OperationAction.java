package ru.popov.systemapp.util;

public enum OperationAction {
    ON_A_CHANGE("На зарядe"),
    ON_DISCHANGE("На разрядe"),
    ON_THE_ISSUE("На выдаче"),
    IN_STOCK("В наличии");

    private String action;

    OperationAction(String action){
        this.action = action;
    }

    @Override
    public String toString() {
        return action;
    }
}
