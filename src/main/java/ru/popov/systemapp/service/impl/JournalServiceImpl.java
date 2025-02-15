package ru.popov.systemapp.service.impl;


import ru.popov.systemapp.dao.FactoryDAO;
import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Journal;
import ru.popov.systemapp.service.JournalService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class JournalServiceImpl implements JournalService {
    @Override
    public void save(Journal j) throws DaoException {
        FactoryDAO.getInstance().getJournalDAO().create(j);
    }

    @Override
    public void update(Journal j) throws DaoException, SQLException {
        FactoryDAO.getInstance().getJournalDAO().update(j);
    }

    @Override
    public List<Journal> getAllDataJournal() throws DaoException {
        return FactoryDAO.getInstance().getJournalDAO().findAll();
    }

    @Override
    public void delete(Journal j) throws DaoException {
        FactoryDAO.getInstance().getJournalDAO().delete(j);
    }

    @Override
    public Journal findById(String name) throws SQLException {
        return FactoryDAO.getInstance().getJournalDAO().findJournalByName(name);
    }

    @Override
    public List getListJournalByDate(LocalDate date) throws SQLException {
        return FactoryDAO.getInstance().getJournalDAO().getAllDataJournalByDate(date);
    }

    @Override
    public void deleteById(Integer id) {
        FactoryDAO.getInstance().getJournalDAO().deleteById(id);
    }
}
