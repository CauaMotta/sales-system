package br.com.ocauamotta.dao.generic;

import br.com.ocauamotta.annotation.KeyType;
import br.com.ocauamotta.annotation.Table;
import br.com.ocauamotta.annotation.TableColumn;
import br.com.ocauamotta.dao.jdbc.ConnectionFactory;
import br.com.ocauamotta.domain.Persistent;
import br.com.ocauamotta.exceptions.*;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class GenericDAO<T extends Persistent, E extends Serializable> implements IGenericDAO<T, E> {

    public enum QueryType {
        INSERT, UPDATE, SELECT, SELECTALL, INSERTPRODUCTS, UPDATEPRODUCTS, SELECTPRODUCTS
    }

    public abstract Class<T> getClassType();

    @Override
    public Boolean register(T entity) throws DaoException, TableException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            QueryType queryType = QueryType.INSERT;
            connection = getConnection();
            ps = connection.prepareStatement(getQuery(queryType), Statement.RETURN_GENERATED_KEYS);
            setQueryParameters(queryType, ps, entity);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if(rs.next()) {
                        Persistent persistent = entity;
                        persistent.setId(rs.getLong(1));
                    }
                }
                return true;
            }
        } catch (SQLException err) {
            throw new DaoException("Erro ao fazer cadastro.", err);
        } finally {
            closeConnection(connection, ps, null);
        }
        return false;
    }

    @Override
    public Integer delete(E value) throws DaoException {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DELETE FROM " + getTableName() + " WHERE " + getFieldKey(getClassType()) + " = ?");
            setQueryParameters(ps, value);
            return ps.executeUpdate();
        } catch (SQLException | TableException err) {
            throw new DaoException("Erro ao fazer a exclusão.", err);
        } finally {
            closeConnection(connection, ps, null);
        }
    }

    @Override
    public Integer update(T entity) throws DaoException {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        try {
            QueryType queryType = QueryType.UPDATE;
            ps = connection.prepareStatement(getQuery(queryType));
            setQueryParameters(queryType, ps, entity);
            return ps.executeUpdate();
        } catch (SQLException | TableException err) {
            throw new DaoException("Erro ao tentar fazer a atualização.", err);
        } finally {
            closeConnection(connection, ps, null);
        }
    }

    @Override
    public T search(E value) throws MultipleRecordsFoundException, TableException, DaoException {
        try {
            validateMultipleRecords(value);
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + getTableName() + " WHERE " + getFieldKey(getClassType()) + " = ?");
            setQueryParameters(ps, value);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                T entity = getClassType().getConstructor(null).newInstance(null);
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(TableColumn.class)) {
                        TableColumn column = field.getAnnotation(TableColumn.class);
                        String dbName = column.dbName();
                        String setJavaName = column.setJavaName();
                        Class<?> classField = field.getType();

                        try {
                            Method method = entity.getClass().getMethod(setJavaName, classField);
                            setValueByType(entity, method, classField, rs, dbName);
                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | UnknownElementTypeException err) {
                            throw new DaoException("Erro ao tentar fazer a consulta ", err);
                        }
                    }
                }
                return entity;
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException |
                 InvocationTargetException | NoSuchMethodException | SecurityException err) {
            throw new DaoException("Erro ao tentar fazer a consulta no banco de dados.", err);
        }

        return null;
    }

    @Override
    public Collection<T> searchAll() throws DaoException {
        List<T> entities = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = getConnection();
            ps = connection.prepareStatement("SELECT * FROM " + getTableName());
            rs = ps.executeQuery();
            while (rs.next()) {
                T entity = getClassType().getConstructor(null).newInstance(null);
                Field[] fields = entity.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(TableColumn.class)) {
                        TableColumn column = field.getAnnotation(TableColumn.class);
                        String dbName = column.dbName();
                        String setJavaName = column.setJavaName();
                        Class<?> classField = field.getType();

                        try {
                            Method method = entity.getClass().getMethod(setJavaName, classField);
                            setValueByType(entity, method, classField, rs, dbName);
                        }  catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | UnknownElementTypeException err) {
                            throw new DaoException("Erro ao listar todos os objetos.", err);
                        }
                    }
                }
                entities.add(entity);
            }
        }catch (SQLException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | TableException err) {
            throw new DaoException("Erro ao listar todos os objetos. ", err);
        } finally {
            closeConnection(connection, ps, rs);
        }
        return entities;
    }

    protected Connection getConnection() throws DaoException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException err) {
            throw new DaoException("Erro ao tentar abrir conexão com o banco de dados.", err);
        }
    }

    private String getFieldKey(Class c) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(KeyType.class) && field.isAnnotationPresent(TableColumn.class)) {
                TableColumn column = field.getAnnotation(TableColumn.class);
                return column.dbName();
            }
        }
        return null;
    }

    private void setParameters(Integer index, T entity, PreparedStatement ps, Class<?> classField, Method method) throws InvocationTargetException, IllegalAccessException, SQLException, UnknownElementTypeException {
        Object value = method.invoke(entity);

        if (classField.equals(Integer.class)) {
            ps.setInt(index, (Integer) value);
        } else if (classField.equals(Long.class)) {
            ps.setLong(index, (Long) value);
        } else if (classField.equals(Double.class)) {
            ps.setDouble(index, (Double) value);
        } else if (classField.equals(Short.class)) {
            ps.setShort(index, (Short) value);
        } else if (classField.equals(BigDecimal.class)) {
            ps.setBigDecimal(index, (BigDecimal) value);
        } else if (classField.equals(String.class)) {
            ps.setString(index, (String) value);
        } else if (classField.equals(Instant.class)) {
            ps.setTimestamp(index, Timestamp.from((Instant) value));
        } else if (classField.isEnum()) {
            ps.setString(index, ((Enum<?>) value).name());
        } else {
            throw new UnknownElementTypeException("Tipo não suportado: " + classField.getName());
        }
    }

    private String getSequenceName() throws TableException {
        return getTableName().replace("tb", "sq");
    }

    private void setValueByType(T entity, Method method, Class<?> classField, ResultSet rs, String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, UnknownElementTypeException {
        if (classField.equals(Integer.class)) {
            Integer value = rs.getInt(fieldName);
            method.invoke(entity, value);
        } else if (classField.equals(Long.class)) {
            Long value = rs.getLong(fieldName);
            method.invoke(entity, value);
        } else if (classField.equals(Double.class)) {
            Double value = rs.getDouble(fieldName);
            method.invoke(entity, value);
        } else if (classField.equals(Short.class)) {
            Short value = rs.getShort(fieldName);
            method.invoke(entity, value);
        } else if (classField.equals(BigDecimal.class)) {
            BigDecimal value = rs.getBigDecimal(fieldName);
            method.invoke(entity, value);
        } else if (classField.equals(String.class)) {
            String value = rs.getString(fieldName);
            method.invoke(entity, value);
        } else {
            throw new UnknownElementTypeException("Tipo da classe não reconhecido." + classField);
        }
    }

    private String getTableName() throws TableException {
        if (getClassType().isAnnotationPresent(Table.class)) {
            Table table = getClassType().getAnnotation(Table.class);
            return table.value();
        } else {
            throw new TableException("Tabela do tipo " + getClassType().getName() + " não foi encontrada.");
        }
    }

    protected Long validateMultipleRecords(E value) throws MultipleRecordsFoundException, TableException, DaoException {
        Connection connection = getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Long count = null;
        try {
            ps = connection.prepareStatement("SELECT count(*) FROM " + getTableName() + " WHERE " + getFieldKey(getClassType()) + " = ?");
            setQueryParameters(ps, value);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getLong(1);
                if (count > 1) {
                    throw new MultipleRecordsFoundException("Foi encontrado mais de um registro na tabela " + getTableName());
                }
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection, ps, rs);
        }
        return count;
    }

    protected void setQueryParameters(PreparedStatement ps, E value) throws SQLException {
        if (value instanceof Long) {
            ps.setLong(1, (Long) value);
        }
        if (value instanceof String) {
            ps.setString(1, (String) value);
        }
    }

    protected void setQueryParameters(QueryType type, PreparedStatement ps, T entity) throws SQLException, DaoException {
        Field[] fields = getClassType().getDeclaredFields();
        Field key = null;
        int index = 1;

        for (Field field : fields) {
            if (type.equals(QueryType.UPDATE) && field.isAnnotationPresent(KeyType.class)
                    && field.isAnnotationPresent(TableColumn.class)) {
                key = field;
                continue;
            }
            if(field.isAnnotationPresent(TableColumn.class)) {
                if (!field.getName().equals("id")) {
                    String setJavaName = field.getAnnotation(TableColumn.class).setJavaName();
                    String getJavaName = setJavaName.replaceFirst("^.", "g");
                    try {
                        Method method = getClassType().getMethod(getJavaName);
                        Class<?> classField = method.getReturnType();
                        setParameters(index, entity, ps, classField, method);
                    } catch (UnknownElementTypeException | InvocationTargetException | NoSuchMethodException | IllegalAccessException err) {
                        throw new DaoException("Erro ao inserir os parametros.", err);
                    }
                    index++;
                }
            }
        }
        if (key != null) {
            String setJavaName = key.getAnnotation(TableColumn.class).setJavaName();
            String getJavaName = setJavaName.replaceFirst("^.", "g");
            Class<?> classField = key.getType();
            try {
                Method method = getClassType().getMethod(getJavaName);
                setParameters(index, entity, ps, classField, method);
            } catch (UnknownElementTypeException | InvocationTargetException | NoSuchMethodException | IllegalAccessException err) {
                throw new DaoException("Erro ao inserir os parametros.", err);
            }
        }
    }

    protected String getQuery(QueryType type) throws TableException {
        StringBuilder sb = new StringBuilder();
        Field[] fields = getClassType().getDeclaredFields();
        List<String> columns = new ArrayList<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(TableColumn.class)) {
                String column = field.getAnnotation(TableColumn.class).dbName();
                columns.add(column);
            }
        }

        if (type.equals(QueryType.INSERT)) {
            sb.append("INSERT INTO " + getTableName() + " (");

            for (int i = 0; i < columns.size(); i++) {
                sb.append(columns.get(i));
                if (i != columns.size() -1) {
                    sb.append(", ");
                }
            }

            sb.append(") VALUES (nextval('" + getSequenceName() + "')" );

            for (int i = 1; i < columns.size(); i++) {
                sb.append(", ?");
            }

            sb.append(")");
        }
        if (type.equals(QueryType.UPDATE)) {
            sb.append("UPDATE " + getTableName() + " SET ");
            String key = getFieldKey(getClassType());

            for (int i = 0; i < columns.size(); i++) {
                if (!columns.get(i).equals(key) && !columns.get(i).equals("id")) {
                    sb.append(columns.get(i) + " = ?");
                    if (i != columns.size() -1) {
                        sb.append(", ");
                    }
                }
            }

            sb.append(" WHERE " + key + " = ?");
        }
        return sb.toString();
    }

    protected void closeConnection(Connection connection, PreparedStatement stm, ResultSet resultSet) {
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
