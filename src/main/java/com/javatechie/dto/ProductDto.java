package com.javatechie.dto;

public class ProductDto {

	private String id;
	private String name;
	private int qty;
	private double price;

	public ProductDto() {
		super();
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getQty() {
		return qty;
	}

	public double getPrice() {
		return price;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ProductDto(String id, String name, int qty, double price) {
		super();
		this.id = id;
		this.name = name;
		this.qty = qty;
		this.price = price;
	}

}
