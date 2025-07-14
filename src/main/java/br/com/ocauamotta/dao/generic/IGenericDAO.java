package br.com.ocauamotta.dao.generic;

import br.com.ocauamotta.domain.Persistent;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericDAO <T extends Persistent, E extends Serializable> {

    public T register(T entity);

    public void delete(E value);

    public T update(T entity);

    public T findById(E value);

    public Collection<T> findAll();
}