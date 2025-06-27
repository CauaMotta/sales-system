package br.com.ocauamotta;

import br.com.ocauamotta.dao.generic.IGenericDAO;
import org.junit.Before;
import org.junit.Test;

import br.com.ocauamotta.dao.ProductDAO;
import br.com.ocauamotta.domain.Product;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.*;

public class ProductDAOTest {

    private IGenericDAO<Product, String> productDao;

    private Product product;

    public ProductDAOTest() {
        productDao = new ProductDAO();
    }

    @Before
    public void init() {
        product = new Product();
        product.setCode("A1");
        product.setName("Produto de Teste");
        product.setDescription("Descrição de teste.");
        product.setCategory("Categoria de teste");
        product.setPrice(BigDecimal.valueOf(2.83));
    }

    @Test
    public void registerProductTest() throws Exception {
        Boolean dbRes = productDao.register(product);
        assertTrue(dbRes);

        productDao.delete(product.getCode());
    }

    @Test
    public void searchProductTest() throws Exception {
        productDao.register(product);

        Product productConsulted = productDao.search(product.getCode());
        assertNotNull(productConsulted);
        assertEquals(product.getCode(), productConsulted.getCode());
        assertEquals(product.getName(), productConsulted.getName());
        assertEquals(product.getDescription(), productConsulted.getDescription());
        assertEquals(product.getPrice(), productConsulted.getPrice());

        productDao.delete(product.getCode());
    }

    @Test
    public void deleteProductTest() throws Exception {
        productDao.register(product);

        Integer dbRes = productDao.delete(product.getCode());
        assertTrue(dbRes == 1);
    }

    @Test
    public void updateProductTest() throws Exception {
        productDao.register(product);

        Product productConsulted = productDao.search(product.getCode());
        assertNotNull(productConsulted);
        assertEquals(product.getCode(), productConsulted.getCode());
        assertEquals(product.getName(), productConsulted.getName());
        assertEquals(product.getDescription(), productConsulted.getDescription());
        assertEquals(product.getPrice(), productConsulted.getPrice());

        product.setName("Teste de Produto");
        product.setDescription("Teste de descrição.");
        Integer dbRes = productDao.update(product);
        assertTrue(dbRes == 1);

        productConsulted = productDao.search(product.getCode());
        assertNotNull(productConsulted);
        assertEquals(product.getCode(), productConsulted.getCode());
        assertEquals(product.getName(), productConsulted.getName());
        assertEquals(product.getDescription(), productConsulted.getDescription());

        productDao.delete(product.getCode());
    }

    @Test
    public void searchAllProductsTest() throws Exception {
        Product product2 = new Product();
        product2.setCode("A2");
        product2.setName("Produto de Teste");
        product2.setDescription("Descrição de teste.");
        product2.setCategory("Categoria de teste");
        product2.setPrice(BigDecimal.valueOf(3.40));

        productDao.register(product);
        productDao.register(product2);

        Collection<Product> list = productDao.searchAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Product product : list) {
            productDao.delete(product.getCode());
            count++;
        }
        assertEquals(list.size(), count);

        list = productDao.searchAll();
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}