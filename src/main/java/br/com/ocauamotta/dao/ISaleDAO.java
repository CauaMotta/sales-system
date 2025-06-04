package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.IGenericDAO;
import br.com.ocauamotta.domain.Sale;
import br.com.ocauamotta.exceptions.KeyTypeNotFoundExcepction;

public interface ISaleDAO extends IGenericDAO<Sale, String> {

    public void finalizeSale(Sale sale) throws KeyTypeNotFoundExcepction;
}