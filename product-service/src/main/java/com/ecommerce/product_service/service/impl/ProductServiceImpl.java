package com.ecommerce.product_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.product_service.dto.ProductRequestDTO;
import com.ecommerce.product_service.dto.ProductResponseDTO;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.model.Product;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.service.ProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	
	private final ProductRepository repository;
	private final ProductMapper mapper;

	@Override
	public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {
		// TODO Auto-generated method stub
		Product product = mapper.toProduct(requestDTO);
		
		Product savedProduct = repository.save(product);
		
		return mapper.toProductResponseDTO(savedProduct);
	}

	@Override
	public List<ProductResponseDTO> getAllsProduts() {
		// TODO Auto-generated method stub
		return repository.findAll()
				.stream()
				.map(mapper::toProductResponseDTO)
				.toList()
				;
	}

	@Override
	public ProductResponseDTO getProductById(String id) {
		// TODO Auto-generated method stub
		Product product = repository.findById(id)
				.orElseThrow(
						() -> new RuntimeException("Producto no encontrado con el id: " + id)
				);
		
		return mapper.toProductResponseDTO(product);
	}

	@Override
	public ProductResponseDTO updateProduct(String id, ProductRequestDTO productRequest) {
		// TODO Auto-generated method stub
		Product product = repository.findById(id)
				.orElseThrow(
						() -> new RuntimeException("Producto no encontrado con el id: " + id)
				);
		
		mapper.updateProductFromRequest(productRequest, product);
		
		Product updatedProduct = repository.save(product);
		
		return mapper.toProductResponseDTO(updatedProduct);
	}

	@Override
	public void deleteProduct(String id) {
		// TODO Auto-generated method stub
		if(!repository.existsById(id)) {
			throw new RuntimeException("Poducto no encontrado con el id:" + id);
		}
			
		
		repository.deleteById(id);
	}

}
