package br.com.ocauamotta.domain;

import java.math.BigDecimal;

import annotation.KeyType;
import br.com.ocauamotta.dao.Persistent;

public class Product implements Persistent {

    @KeyType("getCode")
    private String code;

    private String name;

    private String desc;

    private BigDecimal value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}