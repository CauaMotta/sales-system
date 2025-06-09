package br.com.ocauamotta.dao;

import br.com.ocauamotta.dao.generic.GenericDAO;
import br.com.ocauamotta.domain.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO extends GenericDAO<Client> {

    @Override
    protected String getSqlInsert() {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO tb_client (code, clientName, clientCpf) ");
        sb.append("VALUES (nextval('sq_client'), ?, ?)");
        return sb.toString();
    }

    @Override
    protected void addInsertParameters(PreparedStatement stm, Client client) throws SQLException {
        stm.setString(1, client.getName());
        stm.setString(2, client.getCpf().toString());
    }

    @Override
    protected String getSqlDelete() {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM tb_client ");
        sb.append("WHERE code = ?");
        return sb.toString();
    }

    @Override
    protected void addDeleteParameters(PreparedStatement stm, Client client) throws SQLException {
        stm.setLong(1, client.getCode());
    }

    @Override
    protected String getSqlUpdate() {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE tb_client ");
        sb.append("SET clientName = ?, ");
        sb.append("clientCpf = ? ");
        sb.append("WHERE code = ?");
        return sb.toString();
    }

    @Override
    protected void addUpdateParameters(PreparedStatement stm, Client client) throws SQLException {
        stm.setString(1, client.getName());
        stm.setString(2, client.getCpf().toString());
        stm.setLong(3, client.getCode());
    }

    @Override
    protected String getSqlSelect() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM tb_client ");
        sb.append("WHERE clientCpf = ?");
        return sb.toString();
    }

    @Override
    protected void addSelectParameters(PreparedStatement stm, Long cpf) throws SQLException {
        stm.setString(1, cpf.toString());
    }

    @Override
    protected Client getEntity(ResultSet resultSet) throws SQLException {
        Client client = new Client();
        client.setCode(resultSet.getLong("code"));
        client.setName(resultSet.getString("clientName"));
        client.setCpf(Long.valueOf(resultSet.getString("clientCpf")));

        return client;
    }

    @Override
    protected void addSelectWithCodeParameters(PreparedStatement stm, Long code) throws SQLException {
        stm.setLong(1, code);
    }

    @Override
    protected String getSqlSelectWithCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM tb_client ");
        sb.append("WHERE code = ?");
        return sb.toString();
    }

    @Override
    protected String getSqlSelectAll() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM tb_client");
        return sb.toString();
    }
}