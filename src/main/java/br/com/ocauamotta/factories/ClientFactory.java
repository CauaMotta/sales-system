package br.com.ocauamotta.factories;

import br.com.ocauamotta.domain.Client;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientFactory {

    public static Client convertResultSet(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getLong("clientId"));
        client.setName(rs.getString("clientName"));
        client.setCpf(rs.getLong("clientCpf"));
        client.setEmail(rs.getString("clientEmail"));
        client.setPhone(rs.getLong("clientPhone"));
        client.setAddress(rs.getString("clientAddress"));
        client.setNumber(rs.getInt("clientNumber"));
        client.setCity(rs.getString("clientCity"));
        client.setState(rs.getString("clientState"));
        return client;
    }
}
