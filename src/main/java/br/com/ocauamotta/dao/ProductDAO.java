package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.domain.Product;

public class ProductDAO extends GenericDAO<Product, String> implements IProductDAO {

    public ProductDAO() {
        super();
    }

    @Override
    public void delet() {

    }

    @Override
    public Class<Product> getClassType() {
        return Product.class;
    }

    @Override
    public void dataUpdate(Product entity, Product entityRegistered) {
        entityRegistered.setCode(entity.getCode());
        entityRegistered.setDesc(entity.getDesc());
        entityRegistered.setName(entity.getName());
        entityRegistered.setValue(entity.getValue());
    }
}
