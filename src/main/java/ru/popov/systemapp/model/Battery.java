package ru.popov.systemapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Setter
@Getter
@ToString
public class Battery {
    private Integer id;
    private Integer changeCount;
    private Integer cycles;
    private Integer disChangeCount;
    private LocalDate lastDate;
    private String code;
    private String condition;
    private String descr;
    private String groupBattery;
    private String status;

    public Battery(){}

    public Battery(Integer id, Integer changeCount, Integer cycles,
                   Integer disChangeCount, LocalDate lastDate,
                   String code, String condition, String descr,
                   String groupBattery, String status) {
        this.id = id;
        this.changeCount = changeCount;
        this.cycles = cycles;
        this.disChangeCount = disChangeCount;
        this.lastDate = lastDate;
        this.code = code;
        this.condition = condition;
        this.descr = descr;
        this.groupBattery = groupBattery;
        this.status = status;
    }

}
