package br.com.ocauamotta;

import br.com.ocauamotta.dao.ClientDAO;
import br.com.ocauamotta.dao.ProductDAO;
import br.com.ocauamotta.dao.SaleDAO;
import br.com.ocauamotta.dao.generic.IGenericDAO;
import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.domain.Sale;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SaleDAOTest {

    private IGenericDAO<Sale> saleDao;
    private IGenericDAO<Client> clientDao;
    private IGenericDAO<Product> productDao;

    private Sale sale;
    private Client client;
    private Product product;

    public SaleDAOTest() {
        saleDao = new SaleDAO();
        clientDao = new ClientDAO();
        productDao = new ProductDAO();
    }

    @Before
    public void init() throws Exception {
        client = new Client();
        client.setCpf(11122233345L);
        client.setName("Nome de Teste");
        clientDao.register(client);
        client = clientDao.search(client.getCpf());

        product = new Product();
        product.setId(10L);
        product.setName("Produto de Teste");
        product.setPrice(2.83);
        productDao.register(product);
        product = productDao.search(product.getId());

        sale = new Sale();
        sale.setId(10L);
        sale.setClient(client);
        sale.setProduct(product);
        sale.setQuantity(5);
    }

    @After
    public void cleanDb() throws Exception {
        clientDao.delete(client);
        productDao.delete(product);
    }

    @Test
    public void registerSaleTest() throws Exception {
        Integer dbRes = saleDao.register(sale);
        assertTrue(dbRes == 1);

        sale = saleDao.search(sale.getId());
        saleDao.delete(sale);
    }

    @Test
    public void searchSaleTest() throws Exception{
        saleDao.register(sale);

        Sale saleConsulted = saleDao.search(sale.getId());
        assertNotNull(saleConsulted);
        assertEquals(sale.getId(), saleConsulted.getId());
        assertEquals(sale.getClient().getCode(), saleConsulted.getClientCode());
        assertEquals(sale.getProduct().getCode(), saleConsulted.getProductCode());
        assertEquals(sale.getQuantity(), saleConsulted.getQuantity());

        saleDao.delete(saleConsulted);
    }

    @Test
    public void searchSaleWithCodeTest() throws Exception {
        saleDao.register(sale);

        Sale saleConsulted = saleDao.search(sale.getId());

        saleConsulted = saleDao.searchWithCode(saleConsulted.getCode());
        assertNotNull(saleConsulted);
        assertEquals(sale.getId(), saleConsulted.getId());
        assertEquals(sale.getClient().getCode(), saleConsulted.getClientCode());
        assertEquals(sale.getProduct().getCode(), saleConsulted.getProductCode());
        assertEquals(sale.getQuantity(), saleConsulted.getQuantity());

        saleDao.delete(saleConsulted);
    }

    @Test
    public void deleteSaleTest() throws Exception {
        saleDao.register(sale);

        sale = saleDao.search(sale.getId());
        Integer dbRes = saleDao.delete(sale);
        assertTrue(dbRes == 1);
    }

    @Test
    public void updateSaleTest() throws Exception {
        saleDao.register(sale);

        Sale saleConsulted = saleDao.search(sale.getId());
        assertNotNull(saleConsulted);
        assertEquals(sale.getId(), saleConsulted.getId());
        assertEquals(sale.getClient().getCode(), saleConsulted.getClientCode());
        assertEquals(sale.getProduct().getCode(), saleConsulted.getProductCode());
        assertEquals(sale.getQuantity(), saleConsulted.getQuantity());

        sale = saleConsulted;
        sale.setId(11L);
        sale.setQuantity(10);
        Integer dbRes = saleDao.update(sale);
        assertTrue(dbRes == 1);

        saleConsulted = saleDao.search(sale.getId());
        assertNotNull(saleConsulted);
        assertEquals(sale.getId(), saleConsulted.getId());
        assertEquals(sale.getQuantity(), saleConsulted.getQuantity());

        saleDao.delete(saleConsulted);
    }

    @Test
    public void searchAllSalesTest() throws Exception{
        Sale sale2 = new Sale();
        sale2.setId(11L);
        sale2.setClient(client);
        sale2.setProduct(product);
        sale2.setQuantity(7);

        saleDao.register(sale);
        saleDao.register(sale2);

        List<Sale> list = saleDao.searchAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Sale sale : list) {
            saleDao.delete(sale);
            count++;
        }
        assertEquals(list.size(), count);

        list = saleDao.searchAll();
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}
