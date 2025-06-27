package br.com.ocauamotta.factories;

import br.com.ocauamotta.domain.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductFactory {

    public static Product convertResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getLong("productId"));
        product.setCode(rs.getString("productCode"));
        product.setName(rs.getString("productName"));
        product.setDescription(rs.getString("productDescription"));
        product.setCategory(rs.getString("productCategory"));
        product.setPrice(rs.getBigDecimal("productPrice"));
        return product;
    }
}
