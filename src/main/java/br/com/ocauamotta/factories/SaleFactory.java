package br.com.ocauamotta.factories;

import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.domain.Sale;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SaleFactory {

    public static Sale convertResultSet(ResultSet rs) throws SQLException {
        Client client = ClientFactory.convertResultSet(rs);
        Sale sale = new Sale();
        sale.setId(rs.getLong("saleId"));
        sale.setCode(rs.getString("saleCode"));
        sale.setClient(client);
        sale.setTotalValue(rs.getBigDecimal("totalValue"));
        sale.setDate(rs.getTimestamp("saleDate").toInstant());
        sale.setStatus(Sale.Status.getByName(rs.getString("saleStatus")));
        return sale;
    }
}
