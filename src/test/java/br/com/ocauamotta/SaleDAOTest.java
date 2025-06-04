package br.com.ocauamotta;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.Before;
import org.junit.Test;

import br.com.ocauamotta.dao.ClientDAO;
import br.com.ocauamotta.dao.IClientDAO;
import br.com.ocauamotta.dao.IProductDAO;
import br.com.ocauamotta.dao.ProductDAO;
import br.com.ocauamotta.dao.ISaleDAO;
import br.com.ocauamotta.dao.SaleDAO;
import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.domain.Sale;
import br.com.ocauamotta.domain.Sale.Status;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public class SaleDAOTest {

    private ISaleDAO saleDao;

    private IClientDAO clientDao;

    private IProductDAO productDao;

    private Client client;

    private Product product;

    public SaleDAOTest() {
        saleDao = new SaleDAO();
        clientDao = new ClientDAO();
        productDao = new ProductDAO();
    }

    @Before
    public void init() throws KeyTypeNotFoundExcepction {
        this.client = registerClient();
        this.product = registerProduct("A1", BigDecimal.TEN);
    }

    @Test
    public void search() throws KeyTypeNotFoundExcepction {
        Sale sale = createSale("A1");
        Boolean result = saleDao.register(sale);
        assertTrue(result);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertNotNull(saleConsulted);
        assertEquals(sale.getCode(), saleConsulted.getCode());
    }

    @Test
    public void register() throws KeyTypeNotFoundExcepction {
        Sale sale = createSale("A2");
        Boolean result = saleDao.register(sale);
        assertTrue(result);
        assertTrue(sale.getTotalValue().equals(BigDecimal.valueOf(20)));
        assertTrue(sale.getStatus().equals(Status.INICIADA));
    }

    @Test
    public void cancelSale() throws KeyTypeNotFoundExcepction {
        String saleCode = "A3";
        Sale sale = createSale(saleCode);
        Boolean result = saleDao.register(sale);
        assertTrue(result);
        assertNotNull(sale);
        assertEquals(saleCode, sale.getCode());

        sale.setStatus(Status.CANCELADA);
        saleDao.change(sale);

        Sale saleConsulted = saleDao.search(saleCode);
        assertEquals(saleCode, saleConsulted.getCode());
        assertEquals(Status.CANCELADA, saleConsulted.getStatus());
    }

    @Test
    public void addSameProducts() throws KeyTypeNotFoundExcepction {
        String saleCode = "A4";
        Sale sale = createSale(saleCode);
        Boolean result = saleDao.register(sale);
        assertTrue(result);
        assertNotNull(sale);
        assertEquals(saleCode, sale.getCode());

        Sale saleConsulted = saleDao.search(saleCode);
        saleConsulted.addProduct(product, 1);

        assertTrue(sale.getTotalProducts() == 3);
        assertTrue(sale.getTotalValue().equals(BigDecimal.valueOf(30)));
        assertTrue(sale.getStatus().equals(Status.INICIADA));
    }

    @Test
    public void addDifferentProducts() throws KeyTypeNotFoundExcepction {
        String saleCode = "A5";
        Sale sale = createSale(saleCode);
        Boolean result = saleDao.register(sale);
        assertTrue(result);
        assertNotNull(sale);
        assertEquals(saleCode, sale.getCode());

        Product prod = registerProduct(saleCode, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(saleCode, prod.getCode());

        Sale saleConsulted = saleDao.search(saleCode);
        saleConsulted.addProduct(prod, 1);

        assertTrue(sale.getTotalProducts() == 3);
        assertTrue(sale.getTotalValue().equals(BigDecimal.valueOf(70)));
        assertTrue(sale.getStatus().equals(Status.INICIADA));
    }

    @Test
    public void saveDifferentProduct() throws KeyTypeNotFoundExcepction {
        Sale sale = createSale("A6");
        Boolean result = saleDao.register(sale);
        assertTrue(result);

        Boolean result1 = saleDao.register(sale);
        assertFalse(result1);
        assertTrue(sale.getStatus().equals(Status.INICIADA));
    }

    @Test
    public void removeProduct() throws KeyTypeNotFoundExcepction {
        String saleCode = "A7";
        Sale sale = createSale(saleCode);
        Boolean result = saleDao.register(sale);
        assertTrue(result);
        assertNotNull(sale);
        assertEquals(saleCode, sale.getCode());

        Product prod = registerProduct(saleCode, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(saleCode, prod.getCode());

        Sale saleConsulted = saleDao.search(saleCode);
        saleConsulted.addProduct(prod, 1);
        assertTrue(sale.getTotalProducts() == 3);
        assertTrue(sale.getTotalValue().equals(BigDecimal.valueOf(70)));


        saleConsulted.removeProduct(prod, 1);
        assertTrue(sale.getTotalProducts() == 2);
        assertTrue(sale.getTotalValue().equals(BigDecimal.valueOf(20)));
        assertTrue(sale.getStatus().equals(Status.INICIADA));
    }

    @Test
    public void removeAllProducts() throws KeyTypeNotFoundExcepction {
        String saleCode = "A8";
        Sale sale = createSale(saleCode);
        Boolean result = saleDao.register(sale);
        assertTrue(result);
        assertNotNull(sale);
        assertEquals(saleCode, sale.getCode());

        Product prod = registerProduct(saleCode, BigDecimal.valueOf(50));
        assertNotNull(prod);
        assertEquals(saleCode, prod.getCode());

        Sale saleConsulted = saleDao.search(saleCode);
        saleConsulted.addProduct(prod, 1);
        assertTrue(sale.getTotalProducts() == 3);
        assertTrue(sale.getTotalValue().equals(BigDecimal.valueOf(70)));


        saleConsulted.removeAllProducts();
        assertTrue(sale.getTotalProducts() == 0);
        assertTrue(sale.getTotalValue().equals(BigDecimal.valueOf(0)));
        assertTrue(sale.getStatus().equals(Status.INICIADA));
    }

    @Test
    public void finalizeSale() throws KeyTypeNotFoundExcepction {
        String saleCode = "A9";
        Sale sale = createSale(saleCode);
        Boolean result = saleDao.register(sale);
        assertTrue(result);
        assertNotNull(sale);
        assertEquals(saleCode, sale.getCode());

        saleDao.finalizeSale(sale);

        Sale saleConsulted = saleDao.search(saleCode);
        assertEquals(sale.getCode(), saleConsulted.getCode());
        assertEquals(sale.getStatus(), saleConsulted.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addProductInSaleFinished() throws KeyTypeNotFoundExcepction {
        String saleCode = "A10";
        Sale sale = createSale(saleCode);
        Boolean result = saleDao.register(sale);
        assertTrue(result);
        assertNotNull(sale);
        assertEquals(saleCode, sale.getCode());

        saleDao.finalizeSale(sale);

        Sale saleConsulted = saleDao.search(saleCode);

        assertEquals(sale.getCode(), saleConsulted.getCode());
        assertEquals(sale.getStatus(), saleConsulted.getStatus());

        saleConsulted.addProduct(this.product, 1);
    }

    private Product registerProduct(String code, BigDecimal value) throws KeyTypeNotFoundExcepction {
        Product product = new Product();
        product.setCode(code);
        product.setName("Produto de Teste");
        product.setDesc("Produto Teste");
        product.setValue(value);

        productDao.register(product);
        return product;
    }

    private Client registerClient() throws KeyTypeNotFoundExcepction {
        Client client = new Client();
        client.setCpf(11122233345L);
        client.setName("Nome de Teste");
        client.setAddress("Rua JavaScript");
        client.setNumber(12456);
        client.setCity("Java");
        client.setState("TS");
        client.setPhone(12345678912L);

        clientDao.register(client);
        return client;
    }

    private Sale createSale(String code) {
        Sale sale = new Sale();
        sale.setCode(code);
        sale.setSaleDate(Instant.now());
        sale.setClient(this.client);
        sale.setStatus(Status.INICIADA);
        sale.addProduct(this.product, 2);

        return sale;
    }
}