package br.com.ocauamotta.dao.generic;

import br.com.ocauamotta.domain.Persistent;

import java.sql.SQLException;
import java.util.List;

public interface IGenericDAO <T extends Persistent> {

    public Integer register(T entity) throws Exception;

    public Integer delete(T entity) throws Exception;

    public Integer update(T entity) throws Exception;

    public T search(Long id) throws Exception;

    public T searchWithCode(Long code) throws Exception;

    public List<T> searchAll() throws Exception;
}