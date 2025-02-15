package ru.popov.systemapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Employees {

    private Integer id;
    private String name_empolee;
    private String organ;

    public Employees (){}

    public Employees(String name_empolee, String organ) {
        this.name_empolee = name_empolee;
        this.organ = organ;
    }
}
