package br.com.ocauamotta.domain;

import br.com.ocauamotta.annotation.KeyType;
import br.com.ocauamotta.annotation.Table;
import br.com.ocauamotta.annotation.TableColumn;

import java.math.BigDecimal;

@Table("tb_product")
public class Product implements Persistent {

    @TableColumn(dbName = "id", setJavaName = "setId")
    private Long id;
    @KeyType("getCode")
    @TableColumn(dbName = "productCode", setJavaName = "setCode")
    private String code;
    @TableColumn(dbName = "productName", setJavaName = "setName")
    private String name;
    @TableColumn(dbName = "productDescription", setJavaName = "setDescription")
    private String description;
    @TableColumn(dbName = "productCategory", setJavaName = "setCategory")
    private String category;
    @TableColumn(dbName = "productPrice", setJavaName = "setPrice")
    private BigDecimal price;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}