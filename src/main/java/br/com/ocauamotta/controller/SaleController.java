package br.com.ocauamotta.controller;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

import br.com.ocauamotta.domain.Client;
import br.com.ocauamotta.domain.Product;
import br.com.ocauamotta.domain.ProductQuantity;
import br.com.ocauamotta.domain.Sale;
import br.com.ocauamotta.domain.Sale.Status;
import br.com.ocauamotta.service.ClientService;
import br.com.ocauamotta.service.ProductService;
import br.com.ocauamotta.service.SaleService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class SaleController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Boolean isUpdate;
	private Boolean viewProducts;
	private Integer quantity;
	
	private Sale sale;
	private Collection<Sale> sales;
	
	private Client client;
	private Collection<Client> clients;
	
	private Product product;
	private Collection<Product> products;
	
	@Inject
	private SaleService saleService;
	@Inject
	private ClientService clientService;
	@Inject
	private ProductService productService;
	
	@PostConstruct
	public void init() {
		try {
			reset();
			loadClients();
			loadProducts();
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao listar as vendas."));
		}
	}
	
	public void newSale() {
		try {
			sale.setDate(Instant.now());
			sale.setStatus(Status.INICIADA);
			saleService.register(sale);
			loadSales();
			viewProducts = true;
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao registrar nova venda."));
		}
	}
	
	public void addProduct() {
		try {
			sale.addProduct(product, quantity);
			sale = saleService.update(sale);
			loadSales();
			this.product = null;
			this.quantity = null;
			this.isUpdate = false;
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao adicionar produto."));
		}
	}
	
	public void removeProduct() {
		try {
			sale.removeProduct(product, quantity);
			sale = saleService.update(sale);
			loadSales();
			this.product = null;
			this.quantity = null;
			this.isUpdate = false;
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao remover produto."));
		}
	}
	
	public void deleteProduct(ProductQuantity pq) {
		try {
			sale.removeProduct(pq.getProduct(), pq.getQuantity());
			sale = saleService.update(sale);
			loadSales();
			this.product = null;
			this.quantity = null;
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao deletar produto."));
		}
	}
	
	public void loadProduct(ProductQuantity pq) {
		try {
			this.isUpdate = true;
			this.product = productService.findById(pq.getProduct().getId());
			this.quantity = pq.getQuantity();
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao carregar produto."));
		}
	}
	
	public void edit(Sale sale) {
		try {
			this.sale = sale;
			this.viewProducts = true;
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao carregar venda."));
		}
	}
	
	public void finishSale() {
		try {
			if(!sale.getProducts().isEmpty()) {
				saleService.finishSale(sale);
				reset();
			}
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao finalizar venda."));
		}
	}
	
	public void cancel() {
		try {
			saleService.cancelSale(sale);
			reset();
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao cancelar venda."));
		}
	}
	
	public void cancelEdit() {
		try {
			this.isUpdate = false;
			this.product = null;
			this.quantity = null;
		} catch (Exception err) {
			err.printStackTrace();
			FacesContext.getCurrentInstance().addMessage("growl", new FacesMessage("Erro ao cancelar edição de produto."));
		}
	}
	
	public void reset() {
		this.sale = new Sale();
		loadSales();
		
		isUpdate = false;
		viewProducts = false;
		
		client = null;
		quantity = null;
		product = null;
	}
	
	public String cancelAndRedirect() {
		return "/sales/index.xhtml?faces-redirect=true";
	}
	
	public Boolean validateStatus(Status status) {
		return status == Status.INICIADA;
	}
	
	public Boolean validateStatus() {
		return sale.getStatus() == Status.INICIADA;
	}
	
	private void loadSales() {
		this.sales = saleService.findAll();
	}
	
	private void loadClients() {
		this.clients = clientService.findAll();
	}
	
	private void loadProducts() {
		this.products = productService.findAll();
	}
	
	// Getters and Setters

	public Boolean getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public Boolean getViewProducts() {
		return viewProducts;
	}

	public void setViewProducts(Boolean viewProducts) {
		this.viewProducts = viewProducts;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public Collection<Sale> getSales() {
		return sales;
	}

	public void setSales(Collection<Sale> sales) {
		this.sales = sales;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Collection<Client> getClients() {
		return clients;
	}

	public void setClients(Collection<Client> clients) {
		this.clients = clients;
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
}
