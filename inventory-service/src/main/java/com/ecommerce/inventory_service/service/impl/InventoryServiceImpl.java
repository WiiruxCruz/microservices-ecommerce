package com.ecommerce.inventory_service.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.inventory_service.dto.InventoryRequest;
import com.ecommerce.inventory_service.dto.InventoryResponse;
import com.ecommerce.inventory_service.exception.ResourceNotFoundException;
import com.ecommerce.inventory_service.mapper.InventoryMapper;
import com.ecommerce.inventory_service.model.Inventory;
import com.ecommerce.inventory_service.respository.InventoryRepository;
import com.ecommerce.inventory_service.service.InventoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class InventoryServiceImpl implements InventoryService{
	
	private final InventoryRepository inventoryRepository;
	private final InventoryMapper inventoryMapper;
	
	@Value("${inventory.allow-backorders:false}")
	private boolean allowBackorders;

	@Override
	@Transactional(readOnly = true)
	public boolean isInStock(String sku, Integer quantity) {
		// TODO Auto-generated method stub
		if(allowBackorders) {
			log.warn("MODO BACKORDER ACTIVO: Autorizando stock para SKU: {}", sku);
			return true;
		}
		
		return inventoryRepository.findBySku(sku)
				.map( inventory -> inventory.getQuantity() >= quantity)
				.orElse(false);
	}

	@Override
	@Transactional
	public InventoryResponse createInventory(InventoryRequest inventoryRequest) {
		// TODO Auto-generated method stub
		
		boolean exists = inventoryRepository.existsBySku(inventoryRequest.getSku());
		if(exists) {
			throw new RuntimeException("El inventario pra el SKU " + inventoryRequest.getSku() + " ya existe");
		}
		
		Inventory inventory = inventoryMapper.toModel(inventoryRequest);
		
		Inventory savedInventory = inventoryRepository.save(inventory);
		
		log.info("Inventory {} guardado", savedInventory.getSku());
		
		return inventoryMapper.toResponse(savedInventory);
	}

	@Override
	@Transactional( readOnly = true )
	public List<InventoryResponse> getAllInventory() {
		// TODO Auto-generated method stub
		return inventoryRepository.findAll()
				.stream()
				.map( inventoryMapper :: toResponse )
				.toList();
	}

	@Override
	@Transactional
	public InventoryResponse updateInventory(Long id, InventoryRequest inventoryRequest) {
		// TODO Auto-generated method stub
		Inventory inventory = inventoryRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Inventario", "id", id)
				);
		
		inventory.setSku(inventoryRequest.getSku());
		inventory.setQuantity(inventoryRequest.getQuantity());
		
		Inventory updatedInventory = inventoryRepository.save(inventory);
		
		log.info("Inventario actualizado para el ID: {}", id);
		
		return inventoryMapper.toResponse(updatedInventory);
	}

	@Override
	@Transactional
	public void deleteInventory(Long id) {
		// TODO Auto-generated method stub
		if(!inventoryRepository.existsById(id)) {
			throw new ResourceNotFoundException("inventario", "id", id);
		}
		
		inventoryRepository.deleteById(id);
		
		log.info("Inventario eliminado con ID: {}", id);
	}

	@Override
	public void reduceStock(String sku, Integer quantity) {
		// TODO Auto-generated method stub
		var inventory = inventoryRepository.findBySku(sku)
				.orElseThrow(
						() -> new RuntimeException("Producto no encontrado: " + sku)
				);
		
		if(inventory.getQuantity() < quantity) {
			throw new RuntimeException("Stock insuficiente para: " + sku);
		}
		
		inventory.setQuantity(inventory.getQuantity() - quantity);
		inventoryRepository.save(inventory);
				
	}

}
