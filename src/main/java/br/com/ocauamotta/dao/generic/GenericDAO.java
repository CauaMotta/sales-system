package br.com.ocauamotta.dao.generic;

import br.com.ocauamotta.domain.Persistent;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Collection;

public abstract class GenericDAO<T extends Persistent, E extends Serializable> implements IGenericDAO<T, E> {

    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;
    private Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T register(T entity) {
        openConnection();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        closeConnection();
        return entity;
    }

    @Override
    public void delete(E value) {
       openConnection();
       T entity = entityManager.find(entityClass, value);
       if (entity != null) {
           entityManager.remove(entity);
       }
       entityManager.getTransaction().commit();
       closeConnection();
    }

    @Override
    public T update(T entity) {
        openConnection();
        T merged = entityManager.merge(entity);
        entityManager.getTransaction().commit();
        closeConnection();
        return merged;
    }

    @Override
    public T findById(E value) {
        openConnection();
        T entity = entityManager.find(entityClass, value);
        closeConnection();
        return entity;
    }

    @Override
    public Collection<T> findAll() {
        openConnection();
        CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
        Collection<T> list = entityManager.createQuery(query).getResultList();
        closeConnection();
        return list;
    }

    protected void openConnection() {
        entityManagerFactory = Persistence.createEntityManagerFactory("ExemploJPA");
        entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    protected void closeConnection() {
        entityManager.close();
        entityManagerFactory.close();
    }
}
