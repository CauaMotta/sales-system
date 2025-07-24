package br.com.ocauamotta.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_sale")
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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_seq")
    @SequenceGenerator(name = "sale_seq", sequenceName = "sq_sale", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(length = 100, nullable = false, unique = true)
    private String code;
    @ManyToOne
    @JoinColumn(name = "clientId", foreignKey = @ForeignKey(name = "fk_sale_client"), referencedColumnName = "id", nullable = false)
    private Client client;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<ProductQuantity> products;
    @Column
    private BigDecimal totalValue;
    @Column(nullable = false)
    private Instant date;
    @Column(nullable = false)
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
                            prodQnt.setProduct(product);
                            prodQnt.addProduct(quantity);
                            prodQnt.setSale(this);
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
