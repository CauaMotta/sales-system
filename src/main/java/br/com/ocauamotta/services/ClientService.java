package br.com.ocauamotta.services;

import br.com.ocauamotta.dao.IClientDAO;
import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.services.generic.GenericService;

public class ClientService extends GenericService<Client, Long> implements IClientService {

    public ClientService(IClientDAO clientDAO) {
        super(clientDAO);
    }

    @Override
    public Client searchWithCpf(Long cpf) {
        return this.dao.search(cpf);
    }

}