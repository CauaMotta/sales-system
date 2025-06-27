package br.com.ocauamotta.domain;

import br.com.ocauamotta.annotation.KeyType;
import br.com.ocauamotta.annotation.Table;
import br.com.ocauamotta.annotation.TableColumn;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Table("tb_sale")
public class Sale implements Persistent {

    public enum Status {
        INICIADA, CONCLUIDA, CANCELADA;

        public static Status getByName(String value) {
            for (Status status : Status.values()) {
                if (status.name().equals(value)) {
                    return status;
                }
            }
            return null;
        }
    }

    @TableColumn(dbName = "id", setJavaName = "setId")
    private Long id;
    @KeyType("getCode")
    @TableColumn(dbName = "saleCode", setJavaName = "setCode")
    private String code;
    @TableColumn(dbName = "clientId", setJavaName = "setClientId")
    private Client client;
    private Set<ProductQuantity> products;
    @TableColumn(dbName = "totalValue", setJavaName = "setTotalValue")
    private BigDecimal totalValue;
    @TableColumn(dbName = "saleDate", setJavaName = "setDate")
    private Instant date;
    @TableColumn(dbName = "saleStatus", setJavaName = "setStatus")
    private Status status;

    public Sale() {
        products = new HashSet<>();
    }

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

    public Client getClient() {
        return client;
    }

    public Long getClientId() {
        return client.getId();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<ProductQuantity> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductQuantity> products) {
        this.products = products;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addProduct(Product product, Integer quantity) {
        validateStatus();
        products.stream()
                .filter(prodQnt -> prodQnt.getProduct().getCode().equals(product.getCode()))
                .findFirst()
                .ifPresentOrElse(prodQnt -> prodQnt.addProduct(quantity),
                        () -> {
                            ProductQuantity prodQnt = new ProductQuantity();
                            prodQnt.setId(Long.valueOf(products.size()));
                            prodQnt.setProduct(product);
                            prodQnt.addProduct(quantity);
                            products.add(prodQnt);
                        }
                );
        recalculateTotalValue();
    }

    public void removeProduct(Product product, Integer quantity) {
        validateStatus();
        products.stream()
                .filter(prodQnt -> prodQnt.getProduct().getCode().equals(product.getCode()))
                .findFirst()
                .ifPresent(prodQnt -> {
                    if (prodQnt.getQuantity() > quantity) {
                        prodQnt.removeProduct(quantity);
                    } else {
                        products.removeIf(p -> p.getId().equals(prodQnt.getId()));
                    }
                    recalculateTotalValue();
                });
    }

    public void removeAllProducts() {
        validateStatus();
        products.clear();
        totalValue = BigDecimal.ZERO;
    }

    public Integer getTotalProductQuantity() {
        return products.stream()
                .reduce(0, (partialCountResult, product) -> partialCountResult + product.getQuantity(), Integer::sum);
    }

    private void validateStatus() {
        if (this.status != Status.INICIADA) {
            throw new UnsupportedOperationException("Não é possível modificar a venda.");
        }
    }

    public void recalculateTotalValue() {
        BigDecimal totalValue = BigDecimal.ZERO;
        for (ProductQuantity product : this.products) {
            totalValue = totalValue.add(product.getTotalValue());
        }
        this.totalValue = totalValue;
    }
}