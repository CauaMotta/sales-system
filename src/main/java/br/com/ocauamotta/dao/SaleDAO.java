package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.dao.generic.IGenericDAO;
import br.com.ocauamotta.domain.ProductQuantity;
import br.com.ocauamotta.domain.Sale;
import br.com.ocauamotta.exceptions.DaoException;
import br.com.ocauamotta.exceptions.MultipleRecordsFoundException;
import br.com.ocauamotta.exceptions.TableException;
import br.com.ocauamotta.factories.ProductQuantityFactory;
import br.com.ocauamotta.factories.SaleFactory;

import java.sql.*;
import java.util.*;

public class SaleDAO extends GenericDAO<Sale, String> {

    @Override
    public Class<Sale> getClassType() {
        return Sale.class;
    }

    @Override
    public Boolean register(Sale entity) throws DaoException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = getConnection();
            QueryType queryType = QueryType.INSERT;
            ps = connection.prepareStatement(getQuery(queryType), Statement.RETURN_GENERATED_KEYS);
            setQueryParameters(queryType, ps, entity);
            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()){
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }
                for (ProductQuantity prodQnt : entity.getProducts()) {
                    ps = connection.prepareStatement(getQuery(QueryType.INSERTPRODUCTS), Statement.RETURN_GENERATED_KEYS);
                    setProductsParameters(ps, entity, prodQnt);
                    rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0 ) {
                        try(ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) {
                                prodQnt.setId(rs.getLong(1));
                            }
                        }
                    }
                }
                return true;
            }
        } catch (SQLException | TableException e) {
            throw new DaoException("ERRO CADASTRANDO OBJETO ", e);
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }

    @Override
    public Integer update(Sale entity) throws DaoException {
        Connection connection = getConnection();
        try {
            updateSale(connection, entity);
            synchronizeProducts(connection, entity);
            return 1;
        } catch (SQLException | TableException err) {
            throw new DaoException("Erro ao tentar fazer a atualização.", err);
        } finally {
            closeConnection(connection, null, null);
        }
    }

    @Override
    public Sale search(String value) throws MultipleRecordsFoundException, TableException, DaoException {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            validateMultipleRecords(value);
            connection = getConnection();
            ps = connection.prepareStatement(getQuery(QueryType.SELECT));
            setQueryParameters(ps, value);
            rs = ps.executeQuery();

            if (rs.next()) {
                Sale sale = SaleFactory.convertResultSet(rs);
                searchSaleProducts(connection, sale);
                return sale;
            }
        } catch (SQLException err) {
            throw new DaoException("Erro ao tentar fazer a consulta no banco de dados.", err);
        } finally {
            closeConnection(connection, ps, rs);
        }
        return null;
    }

    @Override
    public Collection<Sale> searchAll() throws DaoException {
        List<Sale> entities = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement(getQuery(QueryType.SELECTALL));
            rs = ps.executeQuery();
            while (rs.next()) {
                Sale sale = SaleFactory.convertResultSet(rs);
                searchSaleProducts(connection, sale);
                entities.add(sale);
            }
        }catch (SQLException | TableException err) {
            throw new DaoException("Erro ao listar todos os objetos. ", err);
        } finally {
            closeConnection(connection, ps, rs);
        }
        return entities;
    }

    public void saleCompleted(Sale sale) throws DaoException {
        sale.setStatus(Sale.Status.CONCLUIDA);
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            ps = connection.prepareStatement("UPDATE tb_sale SET saleStatus = ? WHERE id = ?");
            ps.setString(1, Sale.Status.CONCLUIDA.name());
            ps.setLong(2, sale.getId());
            ps.executeUpdate();
        } catch (SQLException err) {
            throw new DaoException("Erro ao tentar completar a venda.", err);
        } finally {
            closeConnection(connection, ps, null);
        }
    }

    public void cancelSale(Sale sale) throws DaoException {
        sale.setStatus(Sale.Status.CANCELADA);
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement("UPDATE tb_sale SET saleStatus = ? WHERE id = ?");
            ps.setString(1, Sale.Status.CANCELADA.name());
            ps.setLong(2, sale.getId());
            ps.executeUpdate();
        } catch (SQLException err) {
            throw new DaoException("Erro ao tentar cancelar a venda. ", err);
        } finally {
            closeConnection(connection, ps, null);
        }
    }

    private void updateSale(Connection connection, Sale entity) throws TableException, SQLException, DaoException {
        try (PreparedStatement ps = connection.prepareStatement(getQuery(QueryType.UPDATE))) {
            setQueryParameters(QueryType.UPDATE, ps, entity);
            ps.executeUpdate();
        }
    }

    private void synchronizeProducts(Connection connection, Sale entity) throws TableException, SQLException {
        Set<ProductQuantity> currentProducts = getCurrentProducts(connection, entity.getId());
        Set<ProductQuantity> newProducts = new HashSet<>(entity.getProducts());

        removeDeletedProducts(connection, currentProducts, newProducts);

        for (ProductQuantity prodQnt : newProducts) {
            if (prodQnt.getId() != null && currentProducts.contains(prodQnt)) {
                updateProduct(connection, prodQnt);
            } else {
                insetProduct(connection, entity, prodQnt);
            }
        }
    }

    private Set<ProductQuantity> getCurrentProducts(Connection connection, Long saleId) throws TableException, SQLException {
        Set<ProductQuantity> products = new HashSet<>();
        try (PreparedStatement ps = connection.prepareStatement(getQuery(QueryType.SELECTPRODUCTS))) {
            ps.setLong(1, saleId);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()) {
                    products.add(ProductQuantityFactory.convert(rs));
                }
            }
        }
        return products;
    }

    private void removeDeletedProducts(Connection connection, Set<ProductQuantity> currentProducts, Set<ProductQuantity> newProducts) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM tb_productQuantity WHERE id = ?")) {
            for (ProductQuantity prodQnt : currentProducts) {
                if (prodQnt.getId() != null && !newProducts.contains(prodQnt)) {
                    ps.setLong(1, prodQnt.getId());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private void updateProduct(Connection connection, ProductQuantity prodQnt) throws SQLException, TableException {
        try (PreparedStatement ps = connection.prepareStatement(getQuery(QueryType.UPDATEPRODUCTS))) {
            setProductsUpdateParameters(ps, prodQnt);
            ps.executeUpdate();
        }
    }

    private void insetProduct(Connection connection, Sale entity, ProductQuantity prodQnt) throws TableException, SQLException {
        try (PreparedStatement ps = connection.prepareStatement(getQuery(QueryType.INSERTPRODUCTS), Statement.RETURN_GENERATED_KEYS)) {
            setProductsParameters(ps, entity, prodQnt);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try(ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        prodQnt.setId(rs.getLong(1));
                    }
                }
            }

        }
    }

    private void searchSaleProducts(Connection connection, Sale sale) throws TableException, SQLException {
        try (PreparedStatement ps = connection.prepareStatement(getQuery(QueryType.SELECTPRODUCTS))) {
            ps.setLong(1, sale.getId());
            try (ResultSet rs = ps.executeQuery()) {
                Set<ProductQuantity> productQuantities = new HashSet<>();
                while(rs.next()) {
                    ProductQuantity prodQnt = ProductQuantityFactory.convert(rs);
                    productQuantities.add(prodQnt);
                }
                sale.setProducts(productQuantities);
                sale.recalculateTotalValue();
            }
        }
    }

    @Override
    protected String getQuery(QueryType type) throws TableException {
        StringBuilder sb = new StringBuilder(super.getQuery(type));
        if (type.equals(QueryType.SELECT) || type.equals(QueryType.SELECTALL)) {
            sb.append("SELECT S.id AS saleId, S.saleCode, S.totalValue, S.saleDate, S.saleStatus, ");
            sb.append("C.id AS clientId, C.clientName, C.clientCpf, C.clientEmail, C.clientPhone, C.clientAddress, C.clientNumber, C.clientCity, C.clientState ");
            sb.append("FROM tb_sale S ");
            sb.append("INNER JOIN tb_client C ON S.clientId = C.id ");
        }
        if(type.equals(QueryType.SELECT)) {
            sb.append("WHERE S.saleCode = ?");
        }
        if (type.equals(QueryType.SELECTPRODUCTS)) {
            sb.append("SELECT PQ.id AS productQuantityId, PQ.quantity, PQ.totalValue, ");
            sb.append("P.id AS productId, P.productCode, P.productName, P.productDescription, P.productCategory, P.productPrice ");
            sb.append("FROM tb_productQuantity PQ ");
            sb.append("INNER JOIN tb_product P ON P.id = PQ.productId ");
            sb.append("WHERE PQ.saleId = ?");
        }
        if (type.equals(QueryType.INSERTPRODUCTS)) {
            sb.append("INSERT INTO tb_productQuantity ");
            sb.append("(id, productId, saleId, quantity, totalValue)");
            sb.append("VALUES (nextval('sq_productquantity'),?,?,?,?)");
        }
        if (type.equals(QueryType.UPDATEPRODUCTS)) {
            sb.append("UPDATE tb_productQuantity ");
            sb.append("SET quantity = ?, ");
            sb.append("totalValue = ? ");
            sb.append("WHERE id = ?");
        }
        return sb.toString();
    }

    private void setProductsParameters(PreparedStatement ps, Sale entity, ProductQuantity prodQnt) throws SQLException {
        ps.setLong(1, prodQnt.getProduct().getId());
        ps.setLong(2, entity.getId());
        ps.setInt(3, prodQnt.getQuantity());
        ps.setBigDecimal(4, prodQnt.getTotalValue());
    }

    private void setProductsUpdateParameters(PreparedStatement ps, ProductQuantity prodQnt) throws SQLException {
        ps.setInt(1, prodQnt.getQuantity());
        ps.setBigDecimal(2, prodQnt.getTotalValue());
        ps.setLong(3, prodQnt.getId());
    }
}