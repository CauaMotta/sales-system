package br.com.ocauamotta.service;

import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.repository.ProductRepository;
import br.com.ocauamotta.service.generic.GenericService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ProductService extends GenericService<Product, Long> {
	
	public ProductService() {
	}
	
	@Inject
	public ProductService(ProductRepository productRepository) {
		super(productRepository);
	}
}
