package ru.popov.systemapp.service;


import ru.popov.systemapp.exception.DaoException;
import ru.popov.systemapp.model.Journal;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface JournalService {
    void save(Journal j) throws DaoException;
    void update(Journal j) throws DaoException, SQLException;
    List getAllDataJournal() throws DaoException;
    void delete(Journal j) throws DaoException;
    Journal findById(String name) throws SQLException;
    List getListJournalByDate(LocalDate date) throws SQLException;
    void deleteById(Integer id);
}
