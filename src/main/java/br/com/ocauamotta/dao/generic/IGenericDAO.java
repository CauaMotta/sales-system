package br.com.ocauamotta.dao.generic;

import br.com.ocauamotta.domain.Persistent;
import br.com.ocauamotta.exceptions.DaoException;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundException;
import br.com.ocauamotta.exceptions.MultipleRecordsFoundException;
import br.com.ocauamotta.exceptions.TableException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericDAO <T extends Persistent, E extends Serializable> {

    public Boolean register(T entity) throws DaoException, TableException;

    public Integer delete(E value) throws DaoException;

    public Integer update(T entity) throws DaoException;

    public T search(E value) throws MultipleRecordsFoundException, TableException, DaoException;

    public Collection<T> searchAll() throws DaoException;
}