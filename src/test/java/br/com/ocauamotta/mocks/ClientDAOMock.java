package br.com.ocauamotta.mocks;

import br.com.ocauamotta.dao.IClientDAO;
import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

import java.util.Collection;

public class ClientDAOMock implements IClientDAO {
    @Override
    public Boolean register(Client entity) throws KeyTypeNotFoundExcepction {
        return true;
    }

    @Override
    public void delet(Long value) {

    }

    @Override
    public void change(Client entity) throws KeyTypeNotFoundExcepction {

    }

    @Override
    public Client search(Long value) {
        Client client = new Client();
        client.setCpf(value);
        return client;
    }

    @Override
    public Collection<Client> searchAll() {
        return null;
    }
}
