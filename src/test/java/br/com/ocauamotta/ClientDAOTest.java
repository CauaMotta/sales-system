package br.com.ocauamotta;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.ocauamotta.dao.ClientDAO;
import br.com.ocauamotta.dao.IClientDAO;
import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public class ClientDAOTest {

    private IClientDAO dao;

    private Client client;

    public ClientDAOTest() {
        dao = new ClientDAO();
    }

    @Before
    public void init() throws KeyTypeNotFoundExcepction {
        client = new Client();
        client.setCpf(11122233345L);
        client.setName("Nome de Teste");
        client.setAddress("Rua JavaScript");
        client.setNumber(12456);
        client.setCity("Java");
        client.setState("TS");
        client.setPhone(12345678912L);

        dao.register(client);
    }

    @Test
    public void searchClient() {
        Client clientConsulted = dao.search(client.getCpf());
        Assert.assertNotNull(clientConsulted);
    }

    @Test
    public void registerClient() throws KeyTypeNotFoundExcepction {
        client.setCpf(55566677789L);
        Boolean result = dao.register(client);
        Assert.assertTrue(result);
    }

    @Test
    public void deletClient() {
        dao.delet(client.getCpf());
    }

    @Test
    public void updateClient() throws KeyTypeNotFoundExcepction {
        client.setName("Teste");
        dao.change(client);
        Assert.assertEquals("Teste", client.getName());
    }

    @Test
    public void searchAllClients() {
        Collection<Client> list = dao.searchAll();
        assertTrue(list != null);
        assertTrue(list.size() == 2);
    }
}