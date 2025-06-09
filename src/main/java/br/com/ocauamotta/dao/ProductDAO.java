package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.domain.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAO extends GenericDAO<Product> {
    @Override
    protected void addInsertParameters(PreparedStatement stm, Product product) throws SQLException {
        stm.setString(1, product.getName());
        stm.setDouble(2, product.getPrice());
        stm.setLong(3, product.getId());
    }

    @Override
    protected String getSqlInsert() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO tb_product (code, productName, price, productId) ");
        sb.append("VALUES (nextval('sq_product'), ?, ?, ?)");
        return sb.toString();
    }

    @Override
    protected void addDeleteParameters(PreparedStatement stm, Product product) throws SQLException {
        stm.setLong(1, product.getCode());
    }

    @Override
    protected String getSqlDelete() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM tb_product ");
        sb.append("WHERE code = ?");
        return sb.toString();
    }

    @Override
    protected void addUpdateParameters(PreparedStatement stm, Product product) throws SQLException {
        stm.setString(1, product.getName());
        stm.setDouble(2, product.getPrice());
        stm.setLong(3, product.getId());
        stm.setLong(4, product.getCode());
    }

    @Override
    protected String getSqlUpdate() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE tb_product ");
        sb.append("SET productName = ?, ");
        sb.append("price = ?, ");
        sb.append("productId = ? ");
        sb.append("WHERE code = ?");
        return sb.toString();
    }

    @Override
    protected Product getEntity(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setCode(resultSet.getLong("Code"));
        product.setId(resultSet.getLong("productId"));
        product.setName(resultSet.getString("productName"));
        product.setPrice(resultSet.getDouble("price"));

        return product;
    }

    @Override
    protected void addSelectParameters(PreparedStatement stm, Long id) throws SQLException {
        stm.setLong(1, id);
    }

    @Override
    protected String getSqlSelect() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM tb_product ");
        sb.append("WHERE productId = ?");
        return sb.toString();
    }

    @Override
    protected void addSelectWithCodeParameters(PreparedStatement stm, Long code) throws SQLException {
        stm.setLong(1, code);
    }

    @Override
    protected String getSqlSelectWithCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM tb_product ");
        sb.append("WHERE code = ?");
        return sb.toString();
    }

    @Override
    protected String getSqlSelectAll() throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM tb_product");
        return sb.toString();
    }
}
