package com.ecommerce.product_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ecommerce.product_service.dto.ProductRequestDTO;
import com.ecommerce.product_service.dto.ProductResponseDTO;
import com.ecommerce.product_service.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	
	@Mapping(target = "id", ignore = true)
	Product toProduct(ProductRequestDTO requestDTO);
	
	ProductResponseDTO toResponseDTO(Product product);
}
