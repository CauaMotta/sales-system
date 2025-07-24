package br.com.ocauamotta.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ocauamotta.domain.Product;

public class ProductRepositoryTest {

    private static ProductRepository repository;
    private Product product;

    @BeforeClass
    public static void setUpClass() {
    	repository = new ProductRepository();
    }

    @Before
    public void init() {
        product = createProduct("A1", "Produto de Teste");
    }

    @Test
    public void testSaveAndFindNewProduct() {
        Product entity = repository.register(product);
        assertNotNull(entity);

        Product foundProduct = repository.findById(product.getId());
        assertNotNull(foundProduct);
        assertEquals("A1", foundProduct.getCode());
        assertEquals("Produto de Teste", foundProduct.getName());

        repository.deleteById(product.getId());
    }

    @Test
    public void testRemoveProductById() {
        repository.register(product);
        repository.deleteById(product.getId());

        Product foundProduct = repository.findById(product.getId());
        assertNull(foundProduct);
    }
    
    @Test
    public void testRemoveProduct() {
        repository.register(product);
        repository.delete(product);

        Product foundProduct = repository.findById(product.getId());
        assertNull(foundProduct);
    }

    @Test
    public void testUpdateProduct() {
        repository.register(product);

        Product foundProduct = repository.findById(product.getId());
        assertNotNull(foundProduct);
        assertEquals("A1", foundProduct.getCode());
        assertEquals("Produto de Teste", foundProduct.getName());

        product.setName("Teste de Produto");
        Product entity = repository.update(product);
        assertNotNull(entity);

        foundProduct = repository.findById(product.getId());
        assertNotNull(foundProduct);
        assertEquals("A1", foundProduct.getCode());
        assertEquals("Teste de Produto", foundProduct.getName());

        repository.deleteById(product.getId());
    }

    @Test
    public void testFindAllProducts() throws Exception {
        Product product2 = createProduct("A2", "Produto de Teste");

        repository.register(product);
        repository.register(product2);

        Collection<Product> list = repository.findAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Product product : list) {
            repository.deleteById(product.getId());
            count++;
        }
        assertEquals(list.size(), count);

        list = repository.findAll();
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
