package com.ecommerce.product_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

	@Override
	protected String getDatabaseName() {
		// TODO Auto-generated method stub
		return "product-db";
	}
	
	@Bean
	public MongoClient mongoClient() {
		MongoCredential credential = MongoCredential.createCredential(
				"root", "admin", "password".toCharArray()
				);
		
		MongoClientSettings settings = MongoClientSettings.builder()
				.applyConnectionString( new ConnectionString("mongodb://localhost:27017"))
				.credential(credential)
				.build();
		
		return MongoClients.create(settings);
	}

}
