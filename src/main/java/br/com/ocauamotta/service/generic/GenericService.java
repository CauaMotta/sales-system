package br.com.ocauamotta.service.generic;

import java.io.Serializable;
import java.util.Collection;

import br.com.ocauamotta.domain.Persistent;
import br.com.ocauamotta.repository.generic.IGenericRepository;

public abstract class GenericService<T extends Persistent, E extends Serializable> implements IGenericService<T, E> {

	protected IGenericRepository<T, E> repository;
	
	public GenericService() {
	}
	
	public GenericService(IGenericRepository<T, E> repository) {
		this.repository = repository;
	}
	
	@Override
	public T register(T entity) {
		return this.repository.register(entity);
	}
	
	@Override
	public void deleteById(E value) {
		this.repository.deleteById(value);
	}

	@Override
	public void delete(T entity) {
		this.repository.delete(entity);
	}

	@Override
	public T update(T entity) {
		return this.repository.update(entity);
	}

	@Override
	public T findById(E value) {
		return this.repository.findById(value);
	}

	@Override
	public Collection<T> findAll() {
		return this.repository.findAll();
	}

	public IGenericRepository<T, E> getRepository() {
		return repository;
	}

	public void setRepository(IGenericRepository<T, E> repository) {
		this.repository = repository;
	}
}
