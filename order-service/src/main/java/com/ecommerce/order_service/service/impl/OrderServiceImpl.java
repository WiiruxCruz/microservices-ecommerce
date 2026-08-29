package com.ecommerce.order_service.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.exception.ResourceNotFoundException;
import com.ecommerce.order_service.mapper.OrderMapper;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.service.OrderService;
import com.ecommerce.order_service.service.client.InventoryClient;

import ch.qos.logback.classic.html.UrlCssBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@RefreshScope
public class OrderServiceImpl implements OrderService{
	
	private final OrderRepository orderRepository;
	private final OrderMapper orderMapper;
	//private final WebClient.Builder webClientBuilder;
	private final InventoryClient inventoryClient;
	
	@Value("${order.enabled:true}")
	private boolean ordersEnabled;

	@Override
	@Transactional
	public OrderResponse placeOrder(OrderRequest orderRequest) {
		// TODO Auto-generated method stub
		
		if(!ordersEnabled) {
			log.warn("Pedido rechazado: Servicio deshabilitado por configuración");
			throw new RuntimeException("El servicio de pedidios está actualmente en mantenimiento. Intenté más tarde");
		}
		
		log.info("Colocando nuevo pedido");
		
		Order order = orderMapper.toOrder(orderRequest);
		
		for(var item: order.getOrderLineItemsList()) {
			String sku = item.getSku();
			Integer quantity = item.getQuantity();
			
			try {
				/*
				webClientBuilder.build().put()
						.uri("http://localhost:8082/api/v1/inventory/reduce/" + sku,
								uriBuilder -> uriBuilder.queryParam("quantity", quantity).build())
						.retrieve()
						.bodyToMono(String.class)
						.block()
						;
				*/
				inventoryClient.reduceStock(sku, quantity);
				
			} catch (Exception e) {
				log.error("Error al reducir stock para el producto {} : {}", sku, e.getMessage());
				throw new IllegalArgumentException("No se pudo procesar la orden: Stock insuficiente o " +
						"error de inventario"); 
			}
		}
		
		order.setOrderNumber(UUID.randomUUID().toString());
		
		Order savedOrder = orderRepository.save(order);
		
		log.info("Orden guardada con éxito. ID {}", savedOrder.getId());
		
		return orderMapper.toOrderResponse(savedOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderResponse> getAllOrders() {
		// TODO Auto-generated method stub
		return orderRepository.findAll()
				.stream()
				.map( orderMapper :: toOrderResponse)
				.toList()
				;
	}

	@Override
	@Transactional(readOnly = true)
	public OrderResponse getOrderById(Long id) {
		// TODO Auto-generated method stub
		Order order = orderRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("Orden", "id", id)
				);
				
		return orderMapper.toOrderResponse(order);
	}

	@Override
	@Transactional
	public void deleteOrder(Long id) {
		// TODO Auto-generated method stub
		if(!orderRepository.existsById(id)) {
			throw new ResourceNotFoundException("Orden", "id", id);
		}
		
		orderRepository.deleteById(id);
		
		log.info("Orden eliminada. ID {}", id);
	}

}
