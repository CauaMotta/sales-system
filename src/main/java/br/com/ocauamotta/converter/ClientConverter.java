package br.com.ocauamotta.converter;

import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.service.ClientService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(forClass = Client.class, managed = true)
public class ClientConverter implements Converter<Client> {

	@Inject
	private ClientService clientService;
	
    @Override
    public Client getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            Long id = Long.parseLong(value);
            return clientService.findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Client client) {
        if (client == null || client.getId() == null) {
            return "";
        }
        return String.valueOf(client.getId());
    }
}
