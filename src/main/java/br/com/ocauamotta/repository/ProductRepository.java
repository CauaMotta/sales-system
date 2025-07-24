package br.com.ocauamotta.repository;

import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.repository.generic.GenericRepository;

public class ProductRepository extends GenericRepository<Product, Long> {

    public ProductRepository() {
        super(Product.class);
    }
}
