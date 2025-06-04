package br.com.ocauamotta.services.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.ocauamotta.dao.Persistent;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public interface IGenericService <T extends Persistent, E extends Serializable> {

    public Boolean register(T entity) throws KeyTypeNotFoundExcepction;

    public void delet(E value);

    public void change(T entity) throws KeyTypeNotFoundExcepction;

    public T search(E value);

    public Collection<T> searchAll();
}