package br.com.ocauamotta;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ocauamotta.dao.ClientDAO;
import br.com.ocauamotta.domain.Client;

import java.util.Collection;

import static org.junit.Assert.*;

public class ClientDAOTest {

    private static ClientDAO clientDao;
    private Client client;

    @BeforeClass
    public static void setUpClass() {
        clientDao = new ClientDAO();
    }

    @Before
    public void init() {
        client = createClient("Nome de Teste", 11122233345L);
    }

    @Test
    public void testSaveAndFindNewClient() {
        Client entity = clientDao.register(client);
        assertNotNull(entity);

        Client foundClient = clientDao.findById(client.getId());
        assertNotNull(foundClient);
        assertEquals("Nome de Teste", foundClient.getName());
        assertTrue(foundClient.getCpf().equals(11122233345L));

        clientDao.delete(client.getId());
    }

    @Test
    public void testRemoveClient() {
        clientDao.register(client);
        clientDao.delete(client.getId());

        Client foundClient = clientDao.findById(client.getId());
        assertNull(foundClient);
    }

    @Test
    public void testUpdateClient() {
        clientDao.register(client);

        Client foundClient = clientDao.findById(client.getId());
        assertNotNull(foundClient);
        assertEquals("Nome de Teste", foundClient.getName());
        assertTrue(foundClient.getCpf().equals(11122233345L));

        client.setName("Teste de Nome");
        Client entity = clientDao.update(client);
        assertNotNull(entity);

        foundClient = clientDao.findById(client.getId());
        assertNotNull(foundClient);
        assertEquals("Teste de Nome", foundClient.getName());

        clientDao.delete(client.getId());
    }

    @Test
    public void testFindAllClients() {
        Client client2 = createClient("Teste de Nome", 22245698730L);

        clientDao.register(client);
        clientDao.register(client2);

        Collection<Client> list = clientDao.findAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Client client : list) {
            clientDao.delete(client.getId());
            count++;
        }
        assertEquals(list.size(), count);

        list = clientDao.findAll();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    private Client createClient(String name, Long cpf) {
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
}