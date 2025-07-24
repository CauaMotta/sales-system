package br.com.ocauamotta.service;

import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.repository.ClientRepository;
import br.com.ocauamotta.service.generic.GenericService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class ClientService extends GenericService<Client, Long> {
	
	public ClientService() {
	}
	
	@Inject
	public ClientService(ClientRepository clientRepository) {
		super(clientRepository);
	}
}
