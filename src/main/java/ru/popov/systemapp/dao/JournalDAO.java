package ru.popov.systemapp.dao;

import ru.popov.systemapp.model.Journal;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface JournalDAO extends BaseDAO<Integer, Journal>{
 void deleteById(Integer id);
 List<Journal> getAllDataJournalByDate(LocalDate date) throws SQLException;
 Journal findJournalByName(String name) throws SQLException;
 Journal findJournalByNameAndDate(String name, LocalDate date) throws SQLException;
 List<Journal> findJournalByDateLessNow(LocalDate date);
}
