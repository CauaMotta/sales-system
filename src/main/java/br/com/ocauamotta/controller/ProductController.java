package br.com.ocauamotta.controller;

import java.io.Serializable;
import java.util.Collection;

import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.service.ProductService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ProductController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Product product;
	private Collection<Product> products;
	@Inject
	private ProductService productService;
	private Boolean isUpdate;

	@PostConstruct
	public void init() {
		try {
			this.isUpdate = false;
			this.product = new Product();
			this.products = productService.findAll();
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao listar os productes."));
		}
	}
	
	public void cancel() {
		try {
			this.isUpdate = false;
			this.product = new Product();
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar cancelar ação."));
		}
	}
	
	public void edit(Product product) {
		try {
			this.isUpdate = true;
			this.product = product;
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar editar o producte."));
		}
    } 
	
	public void delete(Product product) {
		try {
			productService.delete(product);
			products.remove(product);
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar excluir o producte."));
		}
    } 
	
	public void add() {
		try {
			productService.register(product);
			this.products = productService.findAll();
			this.product = new Product();
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar cadastrar um novo producte."));
		}
    }
	
    public void update() {
    	try {
			productService.update(this.product);
			cancel();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("O producte foi atualiado com sucesso!"));
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao tentar atualizar o producte."));
		}
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Collection<Product> getProducts() {
		return products;
	}

	public void setProducts(Collection<Product> products) {
		this.products = products;
	}

	public Boolean getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
}
