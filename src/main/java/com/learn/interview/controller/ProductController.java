package com.learn.interview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learn.interview.entity.Product;
import com.learn.interview.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	public ProductService productService;
	
	@GetMapping
	public ResponseEntity<Object> getAllProducts() {
		List<Product> res = productService.getAllProducts();
		if(res != null && res.size() > 0) {
			return  ResponseEntity.status(HttpStatus.OK).body(res);
		}
		else {
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
		}
		
	}
	
	@PostMapping
	public ResponseEntity<Object> saveProduct(@RequestBody Product product) {
		Product res = productService.saveProduct(product);
		if(res!=null) {
			return  ResponseEntity.status(HttpStatus.CREATED).body(res);
		}
		else {
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
		
	}
	
	@PostMapping("/{id}")
	public ResponseEntity<Object> updateProduct(@PathVariable("id") int id, @RequestBody Product product) {
		Product res = productService.updateProduct(id, product);
		if(res!=null) {
			return  ResponseEntity.status(HttpStatus.OK).body(res);
		}
		return null;
	}
}
