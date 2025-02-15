package ru.popov.systemapp.dao;

import ru.popov.systemapp.exception.DaoException;

import java.sql.SQLException;
import java.util.List;

public interface BaseDAO <K, T>{
    List<T> findAll() throws DaoException;
    T findEntityById(K id) throws DaoException, SQLException;
    void delete(T t) throws DaoException;
    void create(T t) throws DaoException;
    void update(T t) throws DaoException, SQLException;
}
