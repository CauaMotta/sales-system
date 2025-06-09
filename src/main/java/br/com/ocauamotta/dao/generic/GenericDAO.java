package br.com.ocauamotta.dao.generic;

import br.com.ocauamotta.dao.jdbc.ConnectionFactory;
import br.com.ocauamotta.domain.Persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDAO<T extends Persistent> implements IGenericDAO<T> {

    @Override
    public Integer register(T entity) throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            connection = ConnectionFactory.getConnection();
            String sql = getSqlInsert();
            stm = connection.prepareStatement(sql);
            addInsertParameters(stm, entity);
            return stm.executeUpdate();
        } catch(Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    protected abstract void addInsertParameters(PreparedStatement stm, T entity) throws SQLException;

    protected abstract String getSqlInsert();

    @Override
    public Integer delete(T entity) throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            connection = ConnectionFactory.getConnection();
            String sql = getSqlDelete();
            stm = connection.prepareStatement(sql);
            addDeleteParameters(stm, entity);
            return stm.executeUpdate();
        } catch(Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    protected abstract void addDeleteParameters(PreparedStatement stm, T entity) throws SQLException;

    protected abstract String getSqlDelete();

    @Override
    public Integer update(T entity) throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        try {
            connection = ConnectionFactory.getConnection();
            String sql = getSqlUpdate();
            stm = connection.prepareStatement(sql);
            addUpdateParameters(stm, entity);
            return stm.executeUpdate();
        } catch(Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, null);
        }
    }

    protected abstract void addUpdateParameters(PreparedStatement stm, T entity) throws SQLException;

    protected abstract String getSqlUpdate();

    @Override
    public T search(Long id) throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        T entity = null;
        try {
            connection = ConnectionFactory.getConnection();
            String sql = getSqlSelect();
            stm = connection.prepareStatement(sql);
            addSelectParameters(stm, id);
            resultSet = stm.executeQuery();

            if (resultSet.next()) {
                entity = getEntity(resultSet);
            }

        } catch(Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, resultSet);
        }
        return entity;
    }

    protected abstract T getEntity(ResultSet resultSet) throws SQLException;

    protected abstract void addSelectParameters(PreparedStatement stm, Long id) throws SQLException;

    protected abstract String getSqlSelect();

    @Override
    public T searchWithCode(Long code) throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        T entity = null;
        try {
            connection = ConnectionFactory.getConnection();
            String sql = getSqlSelectWithCode();
            stm = connection.prepareStatement(sql);
            addSelectWithCodeParameters(stm, code);
            resultSet = stm.executeQuery();

            if (resultSet.next()) {
                entity = getEntity(resultSet);
            }

        } catch(Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, resultSet);
        }
        return entity;
    }

    protected abstract void addSelectWithCodeParameters(PreparedStatement stm, Long code) throws SQLException;

    protected abstract String getSqlSelectWithCode();

    @Override
    public List<T> searchAll() throws Exception {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet resultSet = null;
        List<T> entities = new ArrayList<>();
        try {
            connection = ConnectionFactory.getConnection();
            String sql = getSqlSelectAll();
            stm = connection.prepareStatement(sql);
            resultSet = stm.executeQuery();

            while (resultSet.next()) {
                entities.add(getEntity(resultSet));
            }

        } catch(Exception e) {
            throw e;
        } finally {
            closeConnection(connection, stm, resultSet);
        }
        return entities;
    }

    protected abstract String getSqlSelectAll() throws SQLException;

    private void closeConnection(Connection connection, PreparedStatement stm, ResultSet resultSet) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
