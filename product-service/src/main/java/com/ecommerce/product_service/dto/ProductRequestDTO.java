package com.ecommerce.product_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductRequestDTO(
		@NotBlank(message = "El nombre del producto no puede estar vacio")
		String name,
		
		String description,
		
		@NotNull(message = "El precio es obligatorio")
		@Positive(message = "El precio debe ser mayor a cero")
		BigDecimal price
) {
}
