package br.com.ocauamotta.controller;

import java.io.Serializable;
import java.util.Collection;

import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.service.ClientService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ClientController implements Serializable {

	private static final long serialVersionUID = 1L;
	private Client client;
	private Collection<Client> clients;
	@Inject
	private ClientService clientService;
	private Boolean isUpdate;

	@PostConstruct
	public void init() {
		try {
			this.isUpdate = false;
			this.client = new Client();
			this.clients = clientService.findAll();
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao listar os clientes."));
		}
	}
	
	public void cancel() {
		try {
			this.isUpdate = false;
			this.client = new Client();
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar cancelar ação."));
		}
	}
	
	public void edit(Client client) {
		try {
			this.isUpdate = true;
			this.client = client;
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar editar o cliente."));
		}
    } 
	
	public void delete(Client client) {
		try {
			clientService.delete(client);
			clients.remove(client);
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar excluir o cliente."));
		}
    } 
	
	public void add() {
		try {
			clientService.register(client);
			this.clients = clientService.findAll();
			this.client = new Client();
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar cadastrar um novo cliente."));
		}
    }
	
    public void update() {
    	try {
			clientService.update(this.client);
			cancel();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("O cliente foi atualiado com sucesso!"));
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar atualizar o cliente."));
		}
    }

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Collection<Client> getClients() {
		return clients;
	}

	public void setClients(Collection<Client> clients) {
		this.clients = clients;
	}

	public Boolean getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
}
