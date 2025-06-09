package br.com.ocauamotta;


import br.com.ocauamotta.dao.generic.IGenericDAO;
import org.junit.Before;
import org.junit.Test;

import br.com.ocauamotta.dao.ClientDAO;
import br.com.ocauamotta.domain.Client;

import java.util.List;

import static org.junit.Assert.*;

public class ClientDAOTest {

    private IGenericDAO<Client> clientDao;

    private Client client;

    public ClientDAOTest() {
        clientDao = new ClientDAO();
    }

    @Before
    public void init() {
        client = new Client();
        client.setCpf(11122233345L);
        client.setName("Nome de Teste");
        client.setAddress("Rua JavaScript");
        client.setNumber(12456);
        client.setCity("Java");
        client.setState("TS");
        client.setPhone(12345678912L);
    }

    @Test
    public void registerClientTest() throws Exception {
        Integer dbRes = clientDao.register(client);
        assertTrue(dbRes == 1);

        client = clientDao.search(client.getCpf());
        clientDao.delete(client);
    }

    @Test
    public void searchClientTest() throws Exception {
        clientDao.register(client);

        Client clientConsulted = clientDao.search(client.getCpf());
        assertNotNull(clientConsulted);
        assertEquals(client.getCpf(), clientConsulted.getCpf());
        assertEquals(client.getName(), clientConsulted.getName());

        clientDao.delete(clientConsulted);
    }

    @Test
    public void searchClientWithCodeTest() throws Exception {
        clientDao.register(client);

        Client clientConsulted = clientDao.search(client.getCpf());

        clientConsulted = clientDao.searchWithCode(clientConsulted.getCode());
        assertNotNull(clientConsulted);
        assertEquals(client.getCpf(), clientConsulted.getCpf());
        assertEquals(client.getName(), clientConsulted.getName());

        clientDao.delete(clientConsulted);
    }

    @Test
    public void deleteClientTest() throws Exception {
        clientDao.register(client);

        client = clientDao.search(client.getCpf());
        Integer dbRes = clientDao.delete(client);
        assertTrue(dbRes == 1);
    }

    @Test
    public void updateClientTest() throws Exception {
        clientDao.register(client);

        Client clientConsulted = clientDao.search(client.getCpf());

        assertNotNull(clientConsulted);
        assertEquals(client.getName(), clientConsulted.getName());
        assertEquals(client.getCpf(), clientConsulted.getCpf());

        client = clientConsulted;
        client.setName("Teste de Nome");
        Integer dbRes = clientDao.update(client);
        assertTrue(dbRes == 1);

        clientConsulted = clientDao.search(client.getCpf());

        assertNotNull(clientConsulted);
        assertEquals(client.getName(), clientConsulted.getName());
        assertEquals(client.getCpf(), clientConsulted.getCpf());

        clientDao.delete(clientConsulted);
    }

    @Test
    public void searchAllClientsTest() throws Exception {
        Client client2 = new Client();
        client2.setCpf(22245698730L);
        client2.setName("Nome de Teste");

        clientDao.register(client);
        clientDao.register(client2);

        List<Client> list = clientDao.searchAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Client client : list) {
            clientDao.delete(client);
            count++;
        }
        assertEquals(list.size(), count);

        list = clientDao.searchAll();
        assertNotNull(list);
        assertEquals(0, list.size());
    }
}