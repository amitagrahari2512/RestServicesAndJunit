package com.learn.interview.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("id")
	int id;
	
	@Column(name = "name")
	@JsonProperty("name")
	String name;
	
	@Column(name = "quantity")
	@JsonProperty("quantity")
	int quantity;
	
	@Column(name = "price")
	@JsonProperty("price")
	double price;
	
	
}
