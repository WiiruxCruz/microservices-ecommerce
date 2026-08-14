package com.ecommerce.product_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.product_service.dto.ProductRequestDTO;
import com.ecommerce.product_service.dto.ProductResponseDTO;
import com.ecommerce.product_service.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProductResponseDTO createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
		return productService.createProduct(productRequestDTO);
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponseDTO> getAllProducts(){
		return productService.getAllsProduts();
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProductResponseDTO getProductById(@PathVariable String id) {
		return productService.getProductById(id);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable String id) {
		productService.deleteProduct(id);
	}
	
	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProductResponseDTO updateProduct(@PathVariable String id,
			@RequestBody @Valid ProductRequestDTO productRequestDTO) {
		return productService.updateProduct(id, productRequestDTO);
	}
}
