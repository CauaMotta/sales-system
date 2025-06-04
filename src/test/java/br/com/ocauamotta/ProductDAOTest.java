package br.com.ocauamotta;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.ocauamotta.dao.IProductDAO;
import br.com.ocauamotta.dao.ProductDAO;
import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public class ProductDAOTest {

    private IProductDAO dao;

    private Product product;

    public ProductDAOTest() {
        dao = new ProductDAO();
    }

    @Before
    public void init() throws KeyTypeNotFoundExcepction {
        product = new Product();
        product.setCode("A1");
        product.setName("Produto");
        product.setDesc("Produto de Teste");
        product.setValue(BigDecimal.TEN);

        dao.register(product);
    }

    @Test
    public void searchProduct() {
        Product prod = this.dao.search(this.product.getCode());
        Assert.assertNotNull(prod);
    }

    @Test
    public void registerProduct() throws KeyTypeNotFoundExcepction {
        product.setCode("A2");
        Boolean result = dao.register(product);
        Assert.assertTrue(result);
    }

    @Test
    public void deletProduct() {
        dao.delet(product.getCode());
    }

    @Test
    public void updateProduct() throws KeyTypeNotFoundExcepction {
        product.setName("Produto Teste");
        dao.change(product);

        Assert.assertEquals("Produto Teste", product.getName());
    }

    @Test
    public void searchAllProducts() {
        Collection<Product> list = dao.searchAll();
        assertTrue(list != null);
        assertTrue(list.size() == 2);
    }
}