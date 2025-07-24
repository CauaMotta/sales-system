package br.com.ocauamotta.repository;

import br.com.ocauamotta.domain.Sale;
import br.com.ocauamotta.repository.generic.GenericRepository;

public class SaleRepository extends GenericRepository<Sale, Long> {

    public SaleRepository() {
        super(Sale.class);
    }

    public Sale finishSale(Sale sale) {
        sale.setStatus(Sale.Status.CONCLUIDA);
        return update(sale);
    }

    public Sale cancelSale(Sale sale) {
        sale.setStatus(Sale.Status.CANCELADA);
        return update(sale);
    }
}
