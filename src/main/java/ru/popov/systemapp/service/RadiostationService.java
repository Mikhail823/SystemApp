package ru.popov.systemapp.service;

import ru.popov.systemapp.model.Radiostation;

public interface RadiostationService {

    Radiostation findRadioStationByNumber(String number);
}
