package br.com.ocauamotta.services.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.ocauamotta.dao.Persistent;
import br.com.ocauamotta.dao.generic.IGenericDAO;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public abstract class GenericService<T extends Persistent, E extends Serializable> implements IGenericService<T, E> {

    protected IGenericDAO<T,E> dao;

    public GenericService(IGenericDAO<T,E> dao) {
        this.dao = dao;
    }

    @Override
    public Boolean register(T entity) throws KeyTypeNotFoundExcepction {
        return this.dao.register(entity);
    }

    @Override
    public void delet(E value) {
        this.dao.delet(value);
    }

    @Override
    public void change(T entity) throws KeyTypeNotFoundExcepction {
        this.dao.change(entity);
    }

    @Override
    public T search(E value) {
        return this.dao.search(value);
    }

    @Override
    public Collection<T> searchAll() {
        return this.dao.searchAll();
    }
}