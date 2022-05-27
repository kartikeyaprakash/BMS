package com.cg.challenge4.movieservice.config;

import org.apache.commons.lang3.StringUtils;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
//import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
//import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
//import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
//import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
//import com.amazonaws.services.securitytoken.model.Credentials;
//import com.amazonaws.services.securitytoken.model.GetSessionTokenRequest;
//import com.amazonaws.services.securitytoken.model.GetSessionTokenResult;
import com.amazonaws.util.EC2MetadataUtils;

//@Configuration
//@EnableDynamoDBRepositories(basePackages = "com.cg.challenge4.movieservice.repository")
public class DynamoDBConfig {


    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
    	
        AmazonDynamoDB amazonDynamoDB 
          = new AmazonDynamoDBClient(amazonAWSCredentials());
        
        if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
            amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
        }
    	
//    	AWSSecurityTokenService sts_client = AWSSecurityTokenServiceClientBuilder.standard().withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://sts.us-east-1.amazonaws.com", "us-east-1")).build();
//    	GetSessionTokenRequest session_token_request = new GetSessionTokenRequest();
//    	session_token_request.setDurationSeconds(7200); // optional.
//    	
//    	GetSessionTokenResult session_token_result =
//    		    sts_client.getSessionToken(session_token_request);
//    	
//    	Credentials session_creds = session_token_result.getCredentials();
//    	
//    	BasicSessionCredentials sessionCredentials = new BasicSessionCredentials(
//    			   session_creds.getAccessKeyId(),
//    			   session_creds.getSecretAccessKey(),
//    			   session_creds.getSessionToken());
//
    	
//    	
//    	String clientRegion = "*** Client region ***";
//        String roleARN = "*** ARN for role to be assumed ***";
//        String roleSessionName = "*** Role session name ***";
//        String bucketName = "*** Bucket name ***";
//
//       
//            // Creating the STS client is part of your trusted code. It has
//            // the security credentials you use to obtain temporary security credentials.
//            AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
//                                                    .withCredentials(new ProfileCredentialsProvider())
//                                                    .withRegion(clientRegion)
//                                                    .build();
//
//            // Obtain credentials for the IAM role. Note that you cannot assume the role of an AWS root account;
//            // Amazon S3 will deny access. You must use credentials for an IAM user or an IAM role.
//            AssumeRoleRequest roleRequest = new AssumeRoleRequest()
//                                                    .withRoleArn(roleARN)
//                                                    .withRoleSessionName(roleSessionName);
//            AssumeRoleResult roleResponse = stsClient.assumeRole(roleRequest);
//            Credentials sessionCredentials = roleResponse.getCredentials();
//            
//            // Create a BasicSessionCredentials object that contains the credentials you just retrieved.
//            BasicSessionCredentials awsCredentials = new BasicSessionCredentials(
//                    sessionCredentials.getAccessKeyId(),
//                    sessionCredentials.getSecretAccessKey(),
//                    sessionCredentials.getSessionToken());
//
//            // Provide temporary security credentials so that the Amazon S3 client 
//	    // can send authenticated requests to Amazon S3. You create the client 
//	    // using the sessionCredentials object.
//            AmazonDynamoDB dynamoDb = AmazonDynamoDBClientBuilder.standard()
//                                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
//                                    .withRegion(clientRegion)
//                                    .build();
            return amazonDynamoDB;
//
//
//            // Verify that assuming the role worked and the permissions are set correctly
//            // by getting a set of object keys from the bucket.
//           // ObjectListing objects = s3Client.listObjects(bucketName);
//           // System.out.println("No. of Objects: " + objects.getObjectSummaries().size());
        
     
        
        
//        return AmazonDynamoDBClientBuilder.standard()
//        	      .withRegion(Regions.US_EAST_1)
//        	      .build();


       //return AmazonDynamoDBClientBuilder.standard().withCredentials(InstanceProfileCredentialsProvider.getInstance()).withRegion(Regions.US_EAST_1).build();
        
       // return amazonDynamoDB;
    }

    @Bean
    public AWSCredentials amazonAWSCredentials() {
    	
    	//String accessKey = EC2MetadataUtils.getIAMSecurityCredentials().get("http://169.254.169.254").accessKeyId;
    	//String secretKey= EC2MetadataUtils.getIAMSecurityCredentials().get("http://169.254.169.254").secretAccessKey;
        return new BasicAWSCredentials(
        		amazonAWSAccessKey, amazonAWSSecretKey);
    }
    
//    @Bean 
//    public AWSCredentials envCredentials()
//    {
//    	//return new EnvironmentVariableCredentialsProvider()
//    }
    
}

