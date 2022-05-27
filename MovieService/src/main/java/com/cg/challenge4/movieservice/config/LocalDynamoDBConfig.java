package com.cg.challenge4.movieservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.apache.commons.lang3.StringUtils;
import org.socialsignin.spring.data.dynamodb.mapping.DynamoDBMappingContext;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;

@Configuration
@Profile("dev")
@EnableDynamoDBRepositories(mappingContextRef = "dynamoDBMappingContext", basePackages = "com.cg.challenge4.movieservice.repository")
public class LocalDynamoDBConfig {

	@Value("${amazon.dynamodb.endpoint}")
	private String amazonDynamoDBEndpoint;

	@Value("${amazon.aws.accesskey}")
	private String amazonAWSAccessKey;

	@Value("${amazon.aws.secretkey}")
	private String amazonAWSSecretKey;

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {

		AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());

		if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
			amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);

		}

		return amazonDynamoDB;

	}

	public AWSCredentials amazonAWSCredentials() {

		// String accessKey =
		// EC2MetadataUtils.getIAMSecurityCredentials().get("http://169.254.169.254").accessKeyId;
		// String secretKey=
		// EC2MetadataUtils.getIAMSecurityCredentials().get("http://169.254.169.254").secretAccessKey;
		return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
	}

	@Bean
	public DynamoDBMappingContext dynamoDBMappingContext() {
		return new DynamoDBMappingContext();
	}

}
