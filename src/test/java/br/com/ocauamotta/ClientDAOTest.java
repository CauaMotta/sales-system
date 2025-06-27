package br.com.ocauamotta;

import br.com.ocauamotta.dao.generic.IGenericDAO;
import org.junit.Before;
import org.junit.Test;

import br.com.ocauamotta.dao.ClientDAO;
import br.com.ocauamotta.domain.Client;

import java.util.Collection;

import static org.junit.Assert.*;

public class ClientDAOTest {

    private IGenericDAO<Client, Long> clientDao;

    private Client client;

    public ClientDAOTest() {
        clientDao = new ClientDAO();
    }

    @Before
    public void init() {
        client = new Client();
        client.setName("Nome de Teste");
        client.setCpf(11122233345L);
        client.setEmail("emailteste@gmail.com");
        client.setPhone(12345678912L);
        client.setAddress("Rua JavaScript");
        client.setNumber(12456);
        client.setCity("Java");
        client.setState("TS");
    }

    @Test
    public void registerClientTest() throws Exception {
        Boolean dbRes = clientDao.register(client);
        assertTrue(dbRes);

        clientDao.delete(client.getCpf());
    }

    @Test
    public void searchClientTest() throws Exception {
        clientDao.register(client);

        Client clientConsulted = clientDao.search(client.getCpf());
        assertNotNull(clientConsulted);
        assertEquals(client.getCpf(), clientConsulted.getCpf());
        assertEquals(client.getName(), clientConsulted.getName());

        clientDao.delete(client.getCpf());
    }

    @Test
    public void deleteClientTest() throws Exception {
        clientDao.register(client);

        Integer dbRes = clientDao.delete(client.getCpf());
        assertTrue(dbRes == 1);
    }

    @Test
    public void updateClientTest() throws Exception {
        clientDao.register(client);

        Client clientConsulted = clientDao.search(client.getCpf());

        assertNotNull(clientConsulted);
        assertEquals(client.getName(), clientConsulted.getName());
        assertEquals(client.getCpf(), clientConsulted.getCpf());

        client.setName("Teste de Nome");
        Integer dbRes = clientDao.update(client);
        assertTrue(dbRes == 1);

        clientConsulted = clientDao.search(client.getCpf());

        assertNotNull(clientConsulted);
        assertEquals(client.getName(), clientConsulted.getName());
        assertEquals(client.getCpf(), clientConsulted.getCpf());

        clientDao.delete(client.getCpf());
    }

    @Test
    public void searchAllClientsTest() throws Exception {
        Client client2 = new Client();
        client2.setName("Teste de Nome");
        client2.setCpf(22245698730L);
        client2.setEmail("emailteste@gmail.com");
        client2.setPhone(12345678912L);
        client2.setAddress("Rua JavaScript");
        client2.setNumber(12456);
        client2.setCity("Java");
        client2.setState("TS");

        clientDao.register(client);
        clientDao.register(client2);

        Collection<Client> list = clientDao.searchAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Client client : list) {
            clientDao.delete(client.getCpf());
            count++;
        }
        assertEquals(list.size(), count);

        list = clientDao.searchAll();
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}