package br.com.ocauamotta;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.ocauamotta.mocks.ClientDAOMock;
import br.com.ocauamotta.dao.IClientDAO;
import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;
import br.com.ocauamotta.services.ClientService;
import br.com.ocauamotta.services.IClientService;

public class ClientServiceTest {

    private IClientService clientService;

    private Client client;

    public ClientServiceTest() {
        clientService = new ClientService(new ClientDAOMock());
    }

    @Before
    public void init() {
        client = new Client();
        client.setCpf(12312312312L);
        client.setName("Nome de Teste");
        client.setAddress("Rua JavaScript");
        client.setNumber(12456);
        client.setCity("Java");
        client.setState("TS");
        client.setPhone(12345678912L);
    }

    @Test
    public void searchClient() {
        Client clientConsulted = clientService.searchWithCpf(client.getCpf());
        Assert.assertNotNull(clientConsulted);
    }

    @Test
    public void registerClient() throws KeyTypeNotFoundExcepction {
        Boolean result = clientService.register(client);
        Assert.assertTrue(result);
    }

    @Test
    public void deletClient() {
        clientService.delet(client.getCpf());
    }

    @Test
    public void updateClient() throws KeyTypeNotFoundExcepction {
        client.setName("Teste");
        clientService.change(client);

        Assert.assertEquals("Teste", client.getName());
    }
}