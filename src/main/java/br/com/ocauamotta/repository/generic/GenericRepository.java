package br.com.ocauamotta.repository.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.ocauamotta.domain.Persistent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;

public abstract class GenericRepository<T extends Persistent, E extends Serializable> implements IGenericRepository<T, E> {

	@PersistenceContext
	private EntityManager entityManager;
    private Class<T> entityClass;

    public GenericRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T register(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void deleteById(E value) {
       T entity = entityManager.find(entityClass, value);
       if (entity != null) {
           entityManager.remove(entity);
       }
    }
    
    @Override
    public void delete(T entity) {
       T findEntity = entityManager.find(entityClass, entity.getId());
       if (findEntity != null) {
           entityManager.remove(findEntity);
       }
    }

    @Override
    public T update(T entity) {
        T merged = entityManager.merge(entity);
        return merged;
    }

    @Override
    public T findById(E value) {
        T entity = entityManager.find(entityClass, value);
        return entity;
    }

    @Override
    public Collection<T> findAll() {
        CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(entityClass);
        query.select(query.from(entityClass));
        Collection<T> list = entityManager.createQuery(query).getResultList();
        return list;
    }
}