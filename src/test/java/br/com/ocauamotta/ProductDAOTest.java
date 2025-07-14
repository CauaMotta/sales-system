package br.com.ocauamotta;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ocauamotta.dao.ProductDAO;
import br.com.ocauamotta.domain.Product;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.*;

public class ProductDAOTest {

    private static ProductDAO productDao;
    private Product product;

    @BeforeClass
    public static void setUpClass() {
        productDao = new ProductDAO();
    }

    @Before
    public void init() {
        product = createProduct("A1", "Produto de Teste");
    }

    @Test
    public void testSaveAndFindNewProduct() {
        Product entity = productDao.register(product);
        assertNotNull(entity);

        Product foundProduct = productDao.findById(product.getId());
        assertNotNull(foundProduct);
        assertEquals("A1", foundProduct.getCode());
        assertEquals("Produto de Teste", foundProduct.getName());

        productDao.delete(product.getId());
    }

    @Test
    public void testRemoveProduct() {
        productDao.register(product);
        productDao.delete(product.getId());

        Product foundProduct = productDao.findById(product.getId());
        assertNull(foundProduct);
    }

    @Test
    public void testUpdateProduct() {
        productDao.register(product);

        Product foundProduct = productDao.findById(product.getId());
        assertNotNull(foundProduct);
        assertEquals("A1", foundProduct.getCode());
        assertEquals("Produto de Teste", foundProduct.getName());

        product.setName("Teste de Produto");
        Product entity = productDao.update(product);
        assertNotNull(entity);

        foundProduct = productDao.findById(product.getId());
        assertNotNull(foundProduct);
        assertEquals("A1", foundProduct.getCode());
        assertEquals("Teste de Produto", foundProduct.getName());

        productDao.delete(product.getId());
    }

    @Test
    public void testFindAllProducts() throws Exception {
        Product product2 = createProduct("A2", "Produto de Teste");

        productDao.register(product);
        productDao.register(product2);

        Collection<Product> list = productDao.findAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Product product : list) {
            productDao.delete(product.getId());
            count++;
        }
        assertEquals(list.size(), count);

        list = productDao.findAll();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    private Product createProduct(String code, String name) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setDescription("Descrição de teste.");
        product.setCategory("Categoria de teste");
        product.setPrice(BigDecimal.TEN);
        return product;
    }
}