package ru.popov.systemapp.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Radiostation {

    private Integer id;
    private String brNumber;
    private String zavNumber;
    private String indexRadio;
    private String garnitura;
    private Boolean chexol;

}
