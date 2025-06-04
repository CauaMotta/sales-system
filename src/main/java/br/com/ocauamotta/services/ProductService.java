package br.com.ocauamotta.services;

import br.com.ocauamotta.dao.IProductDAO;
import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.services.generic.GenericService;

public class ProductService extends GenericService<Product, String> implements IProductService {

    public ProductService(IProductDAO dao) {
        super(dao);
    }
}