package br.com.ocauamotta.converter;

import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.service.ProductService;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Inject;

@FacesConverter(forClass = Product.class, managed = true)
public class ProductConverter implements Converter<Product> {

	@Inject
	private ProductService productService;
	
    @Override
    public Product getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            Long id = Long.parseLong(value);
            return productService.findById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Product product) {
        if (product == null || product.getId() == null) {
            return "";
        }
        return String.valueOf(product.getId());
    }
}
