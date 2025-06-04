package br.com.ocauamotta.dao.generic;

import br.com.ocauamotta.dao.Persistent;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericDAO <T extends Persistent, E extends Serializable> {

    public Boolean register(T entity) throws KeyTypeNotFoundExcepction;

    public void delet(E value);

    public void change(T entity) throws KeyTypeNotFoundExcepction;

    public T search(E value);

    public Collection<T> searchAll();
}