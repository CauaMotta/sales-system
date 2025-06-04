package br.com.ocauamotta.mocks;

import java.util.Collection;

import br.com.ocauamotta.dao.IProductDAO;
import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public class ProductDAOMock implements IProductDAO {

    @Override
    public Boolean register(Product entity) throws KeyTypeNotFoundExcepction {
        return true;
    }

    @Override
    public void delet(String value) {

    }

    @Override
    public void change(Product entity) throws KeyTypeNotFoundExcepction {

    }

    @Override
    public Product search(String value) {
        Product product = new Product();
        product.setCode(value);
        return product;
    }

    @Override
    public Collection<Product> searchAll() {
        return null;
    }
}