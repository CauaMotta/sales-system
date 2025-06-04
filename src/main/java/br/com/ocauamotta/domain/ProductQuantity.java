package br.com.ocauamotta.domain;

import java.math.BigDecimal;

public class ProductQuantity {

    private Product product;

    private Integer quantity;

    private BigDecimal totalValue;

    public ProductQuantity() {
        this.quantity = 0;
        this.totalValue = BigDecimal.ZERO;
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

    public void add(Integer quantity) {
        this.quantity += quantity;
        BigDecimal newValue = this.product.getValue().multiply(BigDecimal.valueOf(quantity));
        BigDecimal newTotal = this.totalValue.add(newValue);
        this.totalValue = newTotal;
    }

    public void remove(Integer quantity) {
        this.quantity -= quantity;
        BigDecimal newValue = this.product.getValue().multiply(BigDecimal.valueOf(quantity));
        this.totalValue = this.totalValue.subtract(newValue);
    }
}