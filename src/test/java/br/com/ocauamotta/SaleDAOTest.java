package br.com.ocauamotta;

import br.com.ocauamotta.dao.ClientDAO;
import br.com.ocauamotta.dao.ProductDAO;
import br.com.ocauamotta.dao.SaleDAO;
import br.com.ocauamotta.dao.generic.IGenericDAO;
import br.com.ocauamotta.dao.jdbc.ConnectionFactory;
import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.domain.Sale;
import br.com.ocauamotta.exceptions.DaoException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

import static org.junit.Assert.*;

public class SaleDAOTest {

    private SaleDAO saleDao;
    private IGenericDAO<Client, Long> clientDao;
    private IGenericDAO<Product, String> productDao;

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
        client.setName("Nome de Teste");
        client.setCpf(11122233345L);
        client.setEmail("emailteste@gmail.com");
        client.setPhone(12345678912L);
        client.setAddress("Rua JavaScript");
        client.setNumber(12456);
        client.setCity("Java");
        client.setState("TS");
        clientDao.register(client);

        product = new Product();
        product.setCode("A1");
        product.setName("Produto de Teste");
        product.setDescription("Descrição de teste.");
        product.setCategory("Categoria de teste");
        product.setPrice(BigDecimal.TEN);
        productDao.register(product);

        sale = new Sale();
        sale.setCode("S01");
        sale.setDate(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        sale.setClient(client);
        sale.setStatus(Sale.Status.INICIADA);
        sale.addProduct(product, 2);
    }

    @After
    public void cleanDb() throws Exception {
        executeDelete("DELETE FROM tb_productQuantity");
        executeDelete("DELETE FROM tb_sale");
        executeDelete("DELETE FROM tb_product");
        executeDelete("DELETE FROM tb_client");
    }

    @Test
    public void registerNewSaleTest() throws Exception {
        Boolean dbRes = saleDao.register(sale);
        assertTrue(dbRes);
    }

    @Test
    public void searchSaleTest() throws Exception {
        saleDao.register(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertNotNull(saleConsulted);
        assertEquals(sale.getCode(), saleConsulted.getCode());
        assertEquals(sale.getClientId(), saleConsulted.getClientId());
        assertEquals(sale.getDate(), saleConsulted.getDate());
        assertEquals(sale.getStatus(), saleConsulted.getStatus());
    }

    @Test
    public void searchAllSalesTest() throws Exception {
        Sale sale2 = new Sale();
        sale2.setCode("S02");
        sale2.setDate(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        sale2.setClient(client);
        sale2.setStatus(Sale.Status.INICIADA);
        sale2.addProduct(product, 2);

        saleDao.register(sale);
        saleDao.register(sale2);

        Collection<Sale> list = saleDao.searchAll();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void addMoreProductsTest() throws Exception {
        saleDao.register(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertTrue(saleConsulted.getTotalProductQuantity() == 2);
        BigDecimal totalValue = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        assertEquals(totalValue, saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());

        sale.addProduct(product, 1);
        saleDao.update(sale);

        saleConsulted = saleDao.search(sale.getCode());

        assertTrue(saleConsulted.getTotalProductQuantity() == 3);
        assertEquals(sale.getTotalValue().setScale(2, RoundingMode.HALF_DOWN), saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());
    }

    @Test
    public void addMultipleProductsTest() throws Exception {
        saleDao.register(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertTrue(saleConsulted.getTotalProductQuantity() == 2);
        assertEquals(sale.getTotalValue().setScale(2, RoundingMode.HALF_DOWN), saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());

        Product product2 = new Product();
        product2.setCode("B1");
        product2.setName("Teste");
        product2.setDescription("Descrição.");
        product2.setCategory("Categoria de teste");
        product2.setPrice(BigDecimal.valueOf(5));
        productDao.register(product2);

        sale.addProduct(product2, 2);
        saleDao.update(sale);

        saleConsulted = saleDao.search(sale.getCode());
        assertTrue(saleConsulted.getTotalProductQuantity() == 4);
        assertEquals(sale.getTotalValue().setScale(2, RoundingMode.HALF_DOWN), saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());
    }

    @Test
    public void decreaseProductQuantityTest() throws Exception {
        Product product2 = new Product();
        product2.setCode("B1");
        product2.setName("Teste");
        product2.setDescription("Descrição.");
        product2.setCategory("Categoria de teste");
        product2.setPrice(BigDecimal.valueOf(5));
        productDao.register(product2);
        sale.addProduct(product2, 3);

        saleDao.register(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertTrue(saleConsulted.getTotalProductQuantity() == 5);
        assertEquals(sale.getTotalValue().setScale(2, RoundingMode.HALF_DOWN), saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());

        sale.removeProduct(product, 1);

        saleDao.update(sale);

        saleConsulted = saleDao.search(sale.getCode());
        assertTrue(saleConsulted.getTotalProductQuantity() == 4);
        assertEquals(sale.getTotalValue().setScale(2, RoundingMode.HALF_DOWN), saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());
    }

    @Test
    public void removeProductTest() throws Exception {
        Product product2 = new Product();
        product2.setCode("B1");
        product2.setName("Teste");
        product2.setDescription("Descrição.");
        product2.setCategory("Categoria de teste");
        product2.setPrice(BigDecimal.valueOf(5));
        productDao.register(product2);
        sale.addProduct(product2, 3);

        saleDao.register(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertTrue(saleConsulted.getTotalProductQuantity() == 5);
        assertEquals(sale.getTotalValue().setScale(2, RoundingMode.HALF_DOWN), saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());

        sale.removeProduct(product, 2);

        saleDao.update(sale);

        saleConsulted = saleDao.search(sale.getCode());
        assertTrue(saleConsulted.getTotalProductQuantity() == 3);
        assertEquals(sale.getTotalValue().setScale(2, RoundingMode.HALF_DOWN), saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());
    }

    @Test
    public void removeAllProductTest() throws Exception {
        Product product2 = new Product();
        product2.setCode("B1");
        product2.setName("Teste");
        product2.setDescription("Descrição.");
        product2.setCategory("Categoria de teste");
        product2.setPrice(BigDecimal.valueOf(5));
        productDao.register(product2);
        sale.addProduct(product2, 3);

        saleDao.register(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertTrue(saleConsulted.getTotalProductQuantity() == 5);
        assertEquals(sale.getTotalValue().setScale(2, RoundingMode.HALF_DOWN), saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());

        sale.removeAllProducts();

        saleDao.update(sale);

        saleConsulted = saleDao.search(sale.getCode());
        assertTrue(saleConsulted.getTotalProductQuantity() == 0);
        assertEquals(sale.getTotalValue(), saleConsulted.getTotalValue());
        assertEquals(Sale.Status.INICIADA, saleConsulted.getStatus());
    }

    @Test(expected = DaoException.class)
    public void saveSaleWithCodeExistingTest() throws Exception {
        saleDao.register(sale);
        saleDao.register(sale);
    }

    @Test
    public void finishSaleTest() throws Exception {
        saleDao.register(sale);

        saleDao.saleCompleted(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertNotNull(saleConsulted);
        assertEquals(sale.getId(), saleConsulted.getId());
        assertEquals(Sale.Status.CONCLUIDA, saleConsulted.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void modifySaleCompletedTest() throws Exception {
        saleDao.register(sale);
        saleDao.saleCompleted(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertNotNull(saleConsulted);
        assertEquals(sale.getId(), saleConsulted.getId());
        assertEquals(sale.getCode(), saleConsulted.getCode());
        assertEquals(Sale.Status.CONCLUIDA, saleConsulted.getStatus());

        sale.addProduct(product, 1);
    }

    @Test
    public void cancelSaleTest() throws Exception {
        saleDao.register(sale);

        saleDao.cancelSale(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertNotNull(saleConsulted);
        assertEquals(sale.getId(), saleConsulted.getId());
        assertEquals(Sale.Status.CANCELADA, saleConsulted.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void modifySaleCanceledTest() throws Exception {
        saleDao.register(sale);
        saleDao.cancelSale(sale);

        Sale saleConsulted = saleDao.search(sale.getCode());
        assertNotNull(saleConsulted);
        assertEquals(sale.getId(), saleConsulted.getId());
        assertEquals(sale.getCode(), saleConsulted.getCode());
        assertEquals(Sale.Status.CANCELADA, saleConsulted.getStatus());

        sale.addProduct(product, 1);
    }

    private void executeDelete(String sql) throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException err) {
            throw new DaoException("Erro ao tentar excluir os objetos do banco de dados.", err);
        } finally {
            closeConnection(connection, ps, null);
        }
    }

    private void closeConnection(Connection connection, PreparedStatement stm, ResultSet resultSet) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws DaoException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException err) {
            throw new DaoException("Erro ao tentar abrir conexão com o banco de dados.", err);
        }
    }
}
