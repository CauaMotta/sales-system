package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.dao.generic.IGenericDAO;
import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.domain.Sale;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaleDAO extends GenericDAO<Sale> {

    @Override
    protected void addInsertParameters(PreparedStatement stm, Sale sale) throws SQLException {
        stm.setLong(1,sale.getClient().getCode());
        stm.setLong(2, sale.getProduct().getCode());
        stm.setInt(3, sale.getQuantity());
        stm.setLong(4, sale.getId());
    }

    @Override
    protected String getSqlInsert() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO tb_sales (code, clientCode, productCode, quantity, saleId) ");
        sb.append("VALUES (nextval('sq_sales'), ?, ?, ?, ?)");
        return sb.toString();
    }

    @Override
    protected void addDeleteParameters(PreparedStatement stm, Sale sale) throws SQLException {
        stm.setLong(1, sale.getCode());
    }

    @Override
    protected String getSqlDelete() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM tb_sales ");
        sb.append("WHERE code = ?");
        return sb.toString();
    }

    @Override
    protected void addUpdateParameters(PreparedStatement stm, Sale sale) throws SQLException {
        stm.setLong(1, sale.getClientCode());
        stm.setLong(2, sale.getProductCode());
        stm.setInt(3, sale.getQuantity());
        stm.setLong(4, sale.getId());
        stm.setLong(5, sale.getCode());
    }

    @Override
    protected String getSqlUpdate() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE tb_sales ");
        sb.append("SET clientCode = ?, ");
        sb.append("productCode = ?, ");
        sb.append("quantity = ?, ");
        sb.append("saleId = ? ");
        sb.append("WHERE code = ?");
        return sb.toString();
    }

    @Override
    protected Sale getEntity(ResultSet resultSet) throws SQLException {
        Sale sale = new Sale();
        sale.setCode(resultSet.getLong("code"));
        sale.setClientCode(resultSet.getLong("clientCode"));
        sale.setProductCode(resultSet.getLong("productCode"));
        sale.setQuantity(resultSet.getInt("quantity"));
        sale.setId(resultSet.getLong("saleId"));

        return sale;
    }

    @Override
    protected void addSelectParameters(PreparedStatement stm, Long id) throws SQLException {
        stm.setLong(1, id);
    }

    @Override
    protected String getSqlSelect() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM tb_sales ");
        sb.append("WHERE saleId = ?");
        return sb.toString();
    }

    @Override
    protected void addSelectWithCodeParameters(PreparedStatement stm, Long code) throws SQLException {
        stm.setLong(1, code);
    }

    @Override
    protected String getSqlSelectWithCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM tb_sales ");
        sb.append("WHERE code = ?");
        return sb.toString();
    }

    @Override
    protected String getSqlSelectAll() throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM tb_sales");
        return sb.toString();
    }
}