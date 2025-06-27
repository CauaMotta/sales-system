package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.domain.Product;

public class ProductDAO extends GenericDAO<Product, String> {

    @Override
    public Class<Product> getClassType() {
        return Product.class;
    }
}
