package br.com.ocauamotta;

import br.com.ocauamotta.dao.ClientDAO;
import br.com.ocauamotta.dao.ProductDAO;
import br.com.ocauamotta.dao.SaleDAO;
import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.domain.Sale;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import static org.junit.Assert.*;

public class SaleDAOTest {

    private static SaleDAO saleDao;
    private static ClientDAO clientDao;
    private static ProductDAO productDao;

    private static Client client;
    private static Product product;
    private static Product product2;

    @BeforeClass
    public static void setUpClass() {
        saleDao = new SaleDAO();
        clientDao = new ClientDAO();
        productDao = new ProductDAO();

        client = clientDao.register(createClient("Cliente Teste", 11122233345L));
        product = productDao.register(createProduct("A1", "Produto Teste"));
        product2 = productDao.register(createProduct("B1", "Teste Produto"));
    }

    @After
    public void tearDown() {
        executeDelete("DELETE FROM tb_product_quantity");
        executeDelete("DELETE FROM tb_sale");
    }

    @AfterClass
    public static void tearDownClass() {
        executeDelete("DELETE FROM tb_client");
        executeDelete("DELETE FROM tb_product");
    }

    @Test
    public void testSaveAndFindNewSale() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));
        assertNotNull(sale);

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertEquals("S01", foundSale.getCode());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());
    }

    @Test
    public void testFindAllSales() {
        saleDao.register(createSale("S01", client, product, 2));
        saleDao.register(createSale("S02", client, product, 5));

        Collection<Sale> list = saleDao.findAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Sale sale : list) {
            saleDao.delete(sale.getId());
            count++;
        }
        assertEquals(list.size(), count);

        list = saleDao.findAll();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testAddMoreProducts() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 2);
        BigDecimal totalValue = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());

        sale.addProduct(product, 1);
        saleDao.update(sale);

        foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 3);
        assertEquals(sale.getTotalValue().setScale(2, RoundingMode.HALF_DOWN), foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());
    }

    @Test
    public void testAddMultipleProducts() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 2);
        BigDecimal totalValue = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());

        sale.addProduct(product2, 2);
        saleDao.update(sale);

        foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 4);
        totalValue = BigDecimal.valueOf(40).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());
    }

    @Test
    public void testDecreaseProductQuantity() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));
        sale.addProduct(product2, 2);
        sale = saleDao.update(sale);

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 4);
        BigDecimal totalValue = BigDecimal.valueOf(40).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());

        sale.removeProduct(product, 1);
        sale = saleDao.update(sale);

        foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 3);
        totalValue = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());
    }

    @Test
    public void testRemoveProduct() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));
        sale.addProduct(product2, 2);
        sale = saleDao.update(sale);

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 4);
        BigDecimal totalValue = BigDecimal.valueOf(40).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());

        sale.removeProduct(product, 2);
        sale = saleDao.update(sale);

        foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 2);
        totalValue = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());
    }

    @Test
    public void testRemoveAllProducts() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));
        sale.addProduct(product2, 2);
        sale = saleDao.update(sale);

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 4);
        BigDecimal totalValue = BigDecimal.valueOf(40).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());

        sale.removeAllProducts();
        sale = saleDao.update(sale);

        foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 0);
        totalValue = BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.INICIADA, foundSale.getStatus());
    }

    @Test(expected = PersistenceException.class)
    public void testSaveSameSale () {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));
        saleDao.register(sale);
    }

    @Test
    public void testFinishSale() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));

        sale = saleDao.finishSale(sale);

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 2);
        BigDecimal totalValue = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.CONCLUIDA, foundSale.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyCompletedSale() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));
        sale = saleDao.finishSale(sale);

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertEquals("S01", foundSale.getCode());
        assertTrue(foundSale.getTotalProductQuantity() == 2);
        BigDecimal totalValue = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.CONCLUIDA, foundSale.getStatus());

        sale.addProduct(product, 1);
    }

    @Test
    public void testCancelSale() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));

        sale = saleDao.cancelSale(sale);

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertTrue(foundSale.getTotalProductQuantity() == 2);
        BigDecimal totalValue = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.CANCELADA, foundSale.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyCanceledSale() {
        Sale sale = saleDao.register(createSale("S01", client, product, 2));
        sale = saleDao.cancelSale(sale);

        Sale foundSale = saleDao.findById(sale.getId());
        assertNotNull(foundSale);
        assertEquals("S01", foundSale.getCode());
        assertTrue(foundSale.getTotalProductQuantity() == 2);
        BigDecimal totalValue = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, foundSale.getTotalValue());
        assertEquals(Sale.Status.CANCELADA, foundSale.getStatus());

        sale.addProduct(product, 1);
    }

    private static Client createClient(String name, Long cpf) {
        Client client = new Client();
        client.setName(name);
        client.setCpf(cpf);
        client.setEmail("emailteste@gmail.com");
        client.setPhone(12345678912L);
        client.setAddress("Rua JavaScript");
        client.setNumber(12456);
        client.setCity("Java");
        client.setState("TS");
        return client;
    }

    private static Product createProduct(String code, String name) {
        Product product = new Product();
        product.setCode(code);
        product.setName(name);
        product.setDescription("Descrição de teste.");
        product.setCategory("Categoria de teste");
        product.setPrice(BigDecimal.TEN);
        return product;
    }

    private static Sale createSale(String code, Client client, Product product, Integer quantity) {
        Sale sale = new Sale();
        sale.setCode(code);
        sale.setDate(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        sale.setClient(client);
        sale.setStatus(Sale.Status.INICIADA);
        sale.addProduct(product, quantity);
        return sale;
    }

    private static void executeDelete(String sql) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("ExemploJPA");
            entityManager = entityManagerFactory.createEntityManager();

            entityManager.getTransaction().begin();
            entityManager.createNativeQuery(sql).executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
            if (entityManagerFactory != null) entityManagerFactory.close();
        }
    }
}
