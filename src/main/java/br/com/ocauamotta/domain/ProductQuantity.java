package br.com.ocauamotta.domain;

import br.com.ocauamotta.annotation.Table;
import br.com.ocauamotta.annotation.TableColumn;

import java.math.BigDecimal;
import java.util.Objects;

@Table("tb_product_quantity")
public class ProductQuantity implements Persistent {

    @TableColumn(dbName = "id", setJavaName = "setId")
    private Long id;
    private Product product;
    @TableColumn(dbName = "quantity", setJavaName = "setQuantity")
    private Integer quantity;
    @TableColumn(dbName = "totalValue", setJavaName = "setTotalValue")
    private BigDecimal totalValue;

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
