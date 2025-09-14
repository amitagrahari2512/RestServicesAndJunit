package com.learn.interview.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learn.interview.entity.Product;
import com.learn.interview.exception.NotFoundException;
import com.learn.interview.repository.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	public ProductRepository productRepository;
	
	public Product saveProduct(Product product) {
		Product pResponse = productRepository.save(product);
		return pResponse;
	}
	
	public List<Product> getAllProducts() {
		List<Product> listProduct = productRepository.findAll();
		return listProduct;
	}
	
	public Product updateProduct(int productId, Product product) {
		Optional<Product> dbProductOp = productRepository.findById(productId);
		if(dbProductOp.isPresent()) {
			Product dbProduct = dbProductOp.get();
			dbProduct.setName(product.getName());
			dbProduct.setPrice(product.getPrice());
			dbProduct.setQuantity(product.getQuantity());
			Product pResponse = productRepository.save(dbProduct);
			return pResponse;
		}
		throw new NotFoundException("Record Not found");
	}
}
