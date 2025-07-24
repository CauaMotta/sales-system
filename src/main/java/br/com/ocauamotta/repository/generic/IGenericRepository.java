package br.com.ocauamotta.repository.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.ocauamotta.domain.Persistent;

public interface IGenericRepository <T extends Persistent, E extends Serializable> {

    public T register(T entity);

    public void deleteById(E value);
    
    public void delete(T entity);

    public T update(T entity);

    public T findById(E value);

    public Collection<T> findAll();
}