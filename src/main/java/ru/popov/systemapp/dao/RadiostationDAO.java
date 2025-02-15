package ru.popov.systemapp.dao;

import ru.popov.systemapp.model.Battery;
import ru.popov.systemapp.model.Radiostation;

public interface RadiostationDAO extends BaseDAO<Integer, Radiostation> {
    Radiostation findRadioStationByNumber(String number);
}
