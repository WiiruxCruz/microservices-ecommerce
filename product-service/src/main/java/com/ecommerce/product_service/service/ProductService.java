package com.ecommerce.product_service.service;

import java.util.List;

import com.ecommerce.product_service.dto.ProductRequestDTO;
import com.ecommerce.product_service.dto.ProductResponseDTO;

public interface ProductService {
	ProductResponseDTO createProduct(ProductRequestDTO requestDTO);
	List<ProductResponseDTO> getAllsProduts();
	ProductResponseDTO getProductById(String id);
	ProductResponseDTO updateProduct(String id, ProductRequestDTO productRequest);
	void deleteProduct(String id);
}
