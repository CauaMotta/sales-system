package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.domain.Client;

public class ClientDAO extends GenericDAO<Client, Long> implements IClientDAO {

    public ClientDAO() {
        super();
    }

    @Override
    public void delet() {

    }

    @Override
    public Class<Client> getClassType() {
        return Client.class;
    }

    @Override
    public void dataUpdate(Client entity, Client entityRegistered) {
        entityRegistered.setCity(entity.getCity());
        entityRegistered.setCpf(entity.getCpf());
        entityRegistered.setAddress(entity.getAddress());
        entityRegistered.setState(entity.getState());
        entityRegistered.setName(entity.getName());
        entityRegistered.setNumber(entity.getNumber());
        entityRegistered.setPhone(entity.getPhone());

    }
}