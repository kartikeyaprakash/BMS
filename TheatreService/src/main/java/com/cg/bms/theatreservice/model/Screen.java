package com.cg.bms.theatreservice.model;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.Document;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@DynamoDBTable(tableName = "Screen")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Screen {
	
	@DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String _id;
	
	@DynamoDBAttribute
	private List<String> showTimeIds;
	
	@DynamoDBAttribute
	private String theatreId;
	
	@DynamoDBAttribute
	private List<String> seatIds;


}
