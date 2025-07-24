package br.com.ocauamotta.service;

import br.com.ocauamotta.domain.Sale;
import br.com.ocauamotta.repository.SaleRepository;
import br.com.ocauamotta.service.generic.GenericService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class SaleService extends GenericService<Sale, Long> {
	
	public SaleService() {
	}
	
	@Inject
	public SaleService(SaleRepository saleRepository) {
		super(saleRepository);
	}
}
