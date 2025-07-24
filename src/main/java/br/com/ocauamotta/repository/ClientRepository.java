package br.com.ocauamotta.repository;

import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.repository.generic.GenericRepository;

public class ClientRepository extends GenericRepository<Client, Long> {

    public ClientRepository() {
        super(Client.class);
    }
}
