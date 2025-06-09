package br.com.ocauamotta;

import br.com.ocauamotta.dao.generic.IGenericDAO;
import org.junit.Before;
import org.junit.Test;

import br.com.ocauamotta.dao.ProductDAO;
import br.com.ocauamotta.domain.Product;

import java.util.List;

import static org.junit.Assert.*;

public class ProductDAOTest {

    private IGenericDAO<Product> productDao;

    private Product product;

    public ProductDAOTest() {
        productDao = new ProductDAO();
    }

    @Before
    public void init() {
        product = new Product();
        product.setId(10L);
        product.setName("Produto de Teste");
        product.setPrice(2.83);
    }

    @Test
    public void registerProductTest() throws Exception {
        Integer dbRes = productDao.register(product);
        assertTrue(dbRes == 1);

        product = productDao.search(product.getId());
        productDao.delete(product);
    }

    @Test
    public void searchProductTest() throws Exception {
        productDao.register(product);

        Product productConsulted = productDao.search(product.getId());
        assertNotNull(productConsulted);
        assertEquals(product.getId(), productConsulted.getId());
        assertEquals(product.getName(), productConsulted.getName());
        assertEquals(product.getPrice(), productConsulted.getPrice());

        productDao.delete(productConsulted);
    }

    @Test
    public void searchProductWithCodeTest() throws Exception {
        productDao.register(product);

        Product productConsulted = productDao.search(product.getId());

        productConsulted = productDao.searchWithCode(productConsulted.getCode());
        assertNotNull(productConsulted);
        assertEquals(product.getId(), productConsulted.getId());
        assertEquals(product.getName(), productConsulted.getName());
        assertEquals(product.getPrice(), productConsulted.getPrice());

        productDao.delete(productConsulted);
    }

    @Test
    public void deleteProductTest() throws Exception {
        productDao.register(product);

        product = productDao.search(product.getId());
        Integer dbRes = productDao.delete(product);
        assertTrue(dbRes == 1);
    }

    @Test
    public void updateProductTest() throws Exception {
        productDao.register(product);

        Product productConsulted = productDao.search(product.getId());
        assertNotNull(productConsulted);
        assertEquals(product.getId(), productConsulted.getId());
        assertEquals(product.getName(), productConsulted.getName());
        assertEquals(product.getPrice(), productConsulted.getPrice());

        product = productConsulted;
        product.setName("Teste de Produto");
        Integer dbRes = productDao.update(product);
        assertTrue(dbRes == 1);

        productConsulted = productDao.search(product.getId());
        assertNotNull(productConsulted);
        assertEquals(product.getId(), productConsulted.getId());
        assertEquals(product.getName(), productConsulted.getName());
        assertEquals(product.getPrice(), productConsulted.getPrice());

        productDao.delete(productConsulted);
    }

    @Test
    public void searchAllProductsTest() throws Exception {
        Product product2 = new Product();
        product2.setId(11L);
        product2.setName("Produto de Teste");
        product2.setPrice(2.50);

        productDao.register(product);
        productDao.register(product2);

        List<Product> list = productDao.searchAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Product product : list) {
            productDao.delete(product);
            count++;
        }
        assertEquals(list.size(), count);

        list = productDao.searchAll();
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}