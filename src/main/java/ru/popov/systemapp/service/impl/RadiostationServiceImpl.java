package ru.popov.systemapp.service.impl;


import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.model.Radiostation;
import ru.popov.systemapp.service.RadiostationService;

public class RadiostationServiceImpl implements RadiostationService {
    @Override
    public Radiostation findRadioStationByNumber(String number) {
        return FactoryDAO.getInstance().getRadioStationDAO().findRadioStationByNumber(number);
    }
}
