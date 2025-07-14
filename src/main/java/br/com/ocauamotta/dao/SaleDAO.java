package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.domain.Sale;

public class SaleDAO extends GenericDAO<Sale, Long> {

    public SaleDAO() {
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