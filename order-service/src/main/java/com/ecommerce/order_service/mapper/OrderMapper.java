package com.ecommerce.order_service.mapper;

import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.ecommerce.order_service.dto.OrderLineItemsRequest;
import com.ecommerce.order_service.dto.OrderLineItemsResponse;
import com.ecommerce.order_service.dto.OrderRequest;
import com.ecommerce.order_service.dto.OrderResponse;
import com.ecommerce.order_service.model.Order;
import com.ecommerce.order_service.model.OrderLineItems;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
	
	//@Mapping(target = "id", ignore = true)
	Order toOrder(OrderRequest orderRequest);
	
	//@Mapping(target = "id", ignore = true)
	OrderLineItems toOrderLineItems(OrderLineItemsRequest orderLineItemsRequest);
	
	OrderResponse toOrderResponse(Order order);
	
	OrderLineItemsResponse toOrderLineItemsResponse(OrderLineItems orderLineItems);
	
}
