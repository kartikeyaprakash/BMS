package com.cg.bms.theatreservice.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
@Profile("prod")
@EnableDynamoDBRepositories(basePackages = "com.cg.bms.theatreservice.repository")
public class CloudDynamoDBConfig {

	
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		// TODO Auto-generated method stub
		return AmazonDynamoDBClientBuilder.standard()
      	      .withRegion(Regions.US_EAST_1)
      	      .build();
	}

}
