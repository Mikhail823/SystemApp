package ru.popov.systemapp.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class ScanDTO {

    private String descr;
    private Integer size;

    @Override
    public String toString() {
        return descr + " " + size;
    }
}
