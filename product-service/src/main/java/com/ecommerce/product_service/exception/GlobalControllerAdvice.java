package com.ecommerce.product_service.exception;

import java.net.URI;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalControllerAdvice {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
		
		problemDetail.setTitle("Recurso no encontrado");
		problemDetail.setType(URI.create("https://api.ecommerce.com/errors/not-found"));
		problemDetail.setProperty("Timestamp", Instant.now());
		
		problemDetail.setProperty("Resource", ex.getResourceName());
		problemDetail.setProperty("Field", ex.getFieldName());
		problemDetail.setProperty("Value", ex.getFieldValue());
		
		return problemDetail;
	}
}
