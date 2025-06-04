package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.domain.Sale;
import br.com.ocauamotta.domain.Sale.Status;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public class SaleDAO extends GenericDAO<Sale, String> implements ISaleDAO {

    @Override
    public Class<Sale> getClassType() {
        return Sale.class;
    }

    @Override
    public void dataUpdate(Sale entity, Sale entityRegistered) {
        entityRegistered.setCode(entity.getCode());
        entityRegistered.setStatus(entity.getStatus());
    }

    @Override
    public void delet() {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public void finalizeSale(Sale sale) throws KeyTypeNotFoundExcepction {
        sale.setStatus(Status.CONCLUIDA);
        super.change(sale);
    }
}