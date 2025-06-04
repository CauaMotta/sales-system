package br.com.ocauamotta.services;

import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public interface IClientService {

    Boolean register(Client client) throws KeyTypeNotFoundExcepction;
    Client searchWithCpf(Long cpf);
    void delet(Long cpf);
    void change(Client client) throws KeyTypeNotFoundExcepction;
}
