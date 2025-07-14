package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.domain.Client;

public class ClientDAO extends GenericDAO<Client, Long> {

    public ClientDAO() {
        super(Client.class);
    }
}