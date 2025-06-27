package br.com.ocauamotta.factories;

import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.domain.ProductQuantity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductQuantityFactory {

    public static ProductQuantity convert(ResultSet rs) throws SQLException {
        Product product = ProductFactory.convertResultSet(rs);
        ProductQuantity prodQnt = new ProductQuantity();
        prodQnt.setId(rs.getLong("productQuantityId"));
        prodQnt.setProduct(product);
        prodQnt.setQuantity(rs.getInt("quantity"));
        prodQnt.setTotalValue(rs.getBigDecimal("totalValue"));
        return prodQnt;
    }
}
