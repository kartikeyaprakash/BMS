package com.cg.challenge4.movieservice.config;

import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
@Profile("prod")
@EnableDynamoDBRepositories(mappingContextRef = "dynamoDBMappingContext", basePackages = "com.cg.challenge4.movieservice.repository")
public class CloudDynamoDBConfig {

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
	}

	@Bean
	public DynamoDBMappingContext dynamoDBMappingContext() {
		return new DynamoDBMappingContext();
	}

}
