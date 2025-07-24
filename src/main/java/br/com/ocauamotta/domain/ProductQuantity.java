package br.com.ocauamotta.domain;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_product_quantity")
public class ProductQuantity implements Persistent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prodQnt_seq")
    @SequenceGenerator(name = "prodQnt_seq", sequenceName = "sq_productQuantity", initialValue = 1, allocationSize = 1)
    private Long id;
    @ManyToOne
    private Product product;
    @Column(nullable = false)
    private Integer quantity;
    @Column
    private BigDecimal totalValue;
    @ManyToOne
    @JoinColumn(name = "saleId", foreignKey = @ForeignKey(name = "fk_prodQnt_sale"), referencedColumnName = "id", nullable = false)
    private Sale sale;

    public ProductQuantity() {
        this.quantity = 0;
        this.totalValue = BigDecimal.ZERO;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public void addProduct(Integer quantity) {
        this.quantity += quantity;
        BigDecimal newValue = this.product.getPrice().multiply(BigDecimal.valueOf(quantity));
        BigDecimal newTotalValue = this.totalValue.add(newValue);

        this.totalValue = newTotalValue;
    }

    public void removeProduct(Integer quantity) {
        this.quantity -= quantity;
        BigDecimal newValue = this.product.getPrice().multiply(BigDecimal.valueOf(quantity));
        this.totalValue = this.totalValue.subtract(newValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductQuantity that = (ProductQuantity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
