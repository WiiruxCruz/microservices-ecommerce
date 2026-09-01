package com.ecommerce.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class GatewayConfig {
	
	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("product-service", r -> r
						.path("/api/v1/product/**")
						.uri("lb://PRODUCT-SERVICE"))
				.route("order-service", r -> r
						.path("/api/v1/order/**")
						.uri("lb://ORDER-SERVICE"))
				.route("inventory-service", r -> r
						.path("/api/v1/inventory/**")
						.uri("lb://INVENTORY-SERVICE"))
				.build();
	}
}
