package br.com.ocauamotta;

import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.domain.ProductQuantity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class App {

    public static void main(String[] args) {
        Product product = new Product();
        product.setCode("A1");
        product.setName("Produto de Teste");
        product.setDescription("Descrição de teste.");
        product.setPrice(BigDecimal.valueOf(2.83));

        ProductQuantity prod1 = new ProductQuantity();
        prod1.setId(1L);
        prod1.setProduct(product);
        prod1.addProduct(2);

        ProductQuantity prod2 = new ProductQuantity();
        prod2.setId(2L);
        prod2.setProduct(product);
        prod2.addProduct(2);

        ProductQuantity prod3 = new ProductQuantity();
        prod3.setId(3L);
        prod3.setProduct(product);
        prod3.addProduct(2);

        Set<ProductQuantity> products = new HashSet<>();
        products.add(prod1);
        products.add(prod2);

        Set<ProductQuantity> products2 = new HashSet<>();
        products2.add(prod1);
        products2.add(prod2);
        products2.add(prod3);

        System.out.println(products);

        for (ProductQuantity prod : products2) {
            Boolean result = products.contains(prod);
            System.out.println(prod.getId());
            System.out.println(result);
        }
    }
}
