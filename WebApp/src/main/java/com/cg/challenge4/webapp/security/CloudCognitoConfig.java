package com.cg.challenge4.webapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@Configuration
@Profile("prod")
public class CloudCognitoConfig {
	
    @Bean
    public CognitoIdentityProviderClient cognitoClient() {

//        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
//
//        return AWSCognitoIdentityProviderClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-1")
//                .build();
    	
    	return CognitoIdentityProviderClient.builder().region(Region.US_EAST_1).build();

    }

}
