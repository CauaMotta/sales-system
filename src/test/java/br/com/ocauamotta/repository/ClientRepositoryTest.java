package br.com.ocauamotta.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.ocauamotta.domain.Client;

public class ClientRepositoryTest {

    private static ClientRepository repository;
    private Client client;

    @BeforeClass
    public static void setUpClass() {
        repository = new ClientRepository();
    }

    @Before
    public void init() {
        client = createClient("Nome de Teste", 11122233345L);
    }

    @Test
    public void testSaveAndFindNewClient() {
        Client entity = repository.register(client);
        assertNotNull(entity);

        Client foundClient = repository.findById(client.getId());
        assertNotNull(foundClient);
        assertEquals("Nome de Teste", foundClient.getName());
        assertTrue(foundClient.getCpf().equals(11122233345L));

        repository.deleteById(client.getId());
    }

    @Test
    public void testRemoveClientById() {
        repository.register(client);
        repository.deleteById(client.getId());

        Client foundClient = repository.findById(client.getId());
        assertNull(foundClient);
    }
    
    @Test
    public void testRemoveClient() {
        repository.register(client);
        repository.delete(client);

        Client foundClient = repository.findById(client.getId());
        assertNull(foundClient);
    }

    @Test
    public void testUpdateClient() {
        repository.register(client);

        Client foundClient = repository.findById(client.getId());
        assertNotNull(foundClient);
        assertEquals("Nome de Teste", foundClient.getName());
        assertTrue(foundClient.getCpf().equals(11122233345L));

        client.setName("Teste de Nome");
        Client entity = repository.update(client);
        assertNotNull(entity);

        foundClient = repository.findById(client.getId());
        assertNotNull(foundClient);
        assertEquals("Teste de Nome", foundClient.getName());

        repository.deleteById(client.getId());
    }

    @Test
    public void testFindAllClients() {
        Client client2 = createClient("Teste de Nome", 22245698730L);

        repository.register(client);
        repository.register(client2);

        Collection<Client> list = repository.findAll();
        assertNotNull(list);
        assertEquals(2, list.size());

        int count = 0;
        for (Client client : list) {
            repository.deleteById(client.getId());
            count++;
        }
        assertEquals(list.size(), count);

        list = repository.findAll();
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
