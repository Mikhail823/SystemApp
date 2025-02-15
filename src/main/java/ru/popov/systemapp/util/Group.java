package ru.popov.systemapp.util;

public enum Group {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    ZIP("ZIP"),
    HP_519("519"),
    KP_824("KP-824"),
    F_519("519F"),
    KP_824F("KP-824F"),
    HP_419F("419F");

    private String group;

    Group(String group){
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String toString() {
        return group;
    }
}
