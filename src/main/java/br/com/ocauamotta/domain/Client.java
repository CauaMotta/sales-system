package br.com.ocauamotta.domain;

import br.com.ocauamotta.annotation.KeyType;
import br.com.ocauamotta.annotation.Table;
import br.com.ocauamotta.annotation.TableColumn;

@Table("tb_client")
public class Client implements Persistent {

    @TableColumn(dbName = "id", setJavaName = "setId")
    private Long id;
    @TableColumn(dbName = "clientName", setJavaName = "setName")
    private String name;
    @KeyType("getCpf")
    @TableColumn(dbName = "clientCpf", setJavaName = "setCpf")
    private Long cpf;
    @TableColumn(dbName = "clientEmail", setJavaName = "setEmail")
    private String email;
    @TableColumn(dbName = "clientPhone", setJavaName = "setPhone")
    private Long phone;
    @TableColumn(dbName = "clientAddress", setJavaName = "setAddress")
    private String address;
    @TableColumn(dbName = "clientNumber", setJavaName = "setNumber")
    private Integer number;
    @TableColumn(dbName = "clientCity", setJavaName = "setCity")
    private String city;
    @TableColumn(dbName = "clientState", setJavaName = "setState")
    private String state;

    public Client() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
