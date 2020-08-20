package com.haulmont.testtask.daos.interfaces;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public interface DAO<T> {
    Optional<T> getById(long id) throws SQLException;

    Set<T> getAll();

    int getStatisticCount();

    void save(T t);

    void update(T t);

    void delete(T t) throws SQLException;
}
