package br.com.ocauamotta;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.ocauamotta.mocks.ProductDAOMock;
import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;
import br.com.ocauamotta.services.IProductService;
import br.com.ocauamotta.services.ProductService;

public class ProductServiceTest {

    private IProductService productService;

    private Product product;

    public ProductServiceTest() {
        productService = new ProductService(new ProductDAOMock());
    }

    @Before
    public void init() {
        product = new Product();
        product.setCode("A1");
        product.setName("Produto");
        product.setDesc("Produto de Teste");
        product.setValue(BigDecimal.TEN);
    }

    @Test
    public void searchProduct() {
        Product prod = this.productService.search(product.getCode());
        Assert.assertNotNull(prod);
    }

    @Test
    public void registerProduct() throws KeyTypeNotFoundExcepction {
        Boolean result = productService.register(product);
        Assert.assertTrue(result);
    }

    @Test
    public void deletProduct() {
        productService.delet(product.getCode());
    }

    @Test
    public void updateProduct() throws KeyTypeNotFoundExcepction {
        product.setName("Produto Teste");
        productService.change(product);

        Assert.assertEquals("Produto Teste", product.getName());
    }
}