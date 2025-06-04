package br.com.ocauamotta.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import annotation.KeyType;
import br.com.ocauamotta.dao.Persistent;

public class Sale implements Persistent {

    public enum Status {
        INICIADA, CONCLUIDA, CANCELADA;
    }

    @KeyType("getCode")
    private String code;

    private Client client;

    private Set<ProductQuantity> products;

    private BigDecimal totalValue;

    private Instant saleDate;

    private Status status;

    public Sale() {
        products = new HashSet<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<ProductQuantity> getProducts() {
        return products;
    }

    public void addProduct(Product product, Integer quantity) {
        statusValidate();
        Optional<ProductQuantity> op =
                products.stream().filter(filter -> filter.getProduct().getCode().equals(product.getCode())).findAny();
        if (op.isPresent()) {
            ProductQuantity productQtd = op.get();
            productQtd.add(quantity);
        } else {
            ProductQuantity prod = new ProductQuantity();
            prod.setProduct(product);
            prod.add(quantity);
            products.add(prod);
        }
        recalculateTotalValue();
    }

    private void statusValidate() {
        if (this.status == Status.CONCLUIDA) {
            throw new UnsupportedOperationException("IMPOSSÍVEL ALTERAR VENDA FINALIZADA");
        }
    }

    public void removeProduct(Product product, Integer quantity) {
        statusValidate();
        Optional<ProductQuantity> op =
                products.stream().filter(filter -> filter.getProduct().getCode().equals(product.getCode())).findAny();
        if (op.isPresent()) {
            ProductQuantity productQtd = op.get();
            if (productQtd.getQuantity()>quantity) {
                productQtd.remove(quantity);
                recalculateTotalValue();
            } else {
                products.remove(op.get());
                recalculateTotalValue();
            }
        }
    }

    public void removeAllProducts() {
        statusValidate();
        products.clear();
        totalValue = BigDecimal.ZERO;
    }

    public Integer getTotalProducts() {
        return products.stream()
                .reduce(0, (partialCountResult, prod) -> partialCountResult + prod.getQuantity(), Integer::sum);
    }

    private void recalculateTotalValue() {
        statusValidate();
        BigDecimal totalValue = BigDecimal.ZERO;
        for (ProductQuantity prod : this.products) {
            totalValue = totalValue.add(prod.getTotalValue());
        }
        this.totalValue = totalValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public Instant getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Instant saleDate) {
        this.saleDate = saleDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}