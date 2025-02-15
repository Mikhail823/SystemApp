package ru.popov.systemapp.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Journal {

    private Integer id;
    private String name;
    private String zav_number;
    private LocalDate dateReceipt;
    private String organization;
    private String equipment;
    private String br_number;
    private String chexol;
    private String changer;
    private String battery;

    public Journal(){}

    public Journal(Integer id, String name, String zavNumber, LocalDate dateReceipt,
                   String organization, String equipment, String brNumber,
                   String chexol, String changer, String battery) {
        this.id = id;
        this.name = name;
        this.zav_number = zavNumber;
        this.dateReceipt = dateReceipt;
        this.organization = organization;
        this.equipment = equipment;
        this.br_number = brNumber;
        this.chexol = chexol;
        this.changer = changer;
        this.battery = battery;
    }

    public Journal(String name, String zavNumber, LocalDate dateReceipt,
                   String organization, String equipment, String brNumber,
                   String chexol, String changer, String battery) {

        this.name = name;
        this.zav_number = zavNumber;
        this.dateReceipt = dateReceipt;
        this.organization = organization;
        this.equipment = equipment;
        this.br_number = brNumber;
        this.chexol = chexol;
        this.changer = changer;
        this.battery = battery;
    }


    @Override
    public String toString() {
        return id + " " + name + " " +  zav_number + " " + dateReceipt + " " +
                organization + " " +  equipment + " " + br_number  + " "
                +  chexol + " " + changer + " " +  battery + "\n";
    }
}
