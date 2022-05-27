package com.cg.challenge4.webapp.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUserGlobalSignOutRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;


@Service
public class CustomLogoutHandler implements LogoutHandler {
	
    @Autowired
    private CognitoIdentityProviderClient cognitoClient;


    @Value(value = "${aws.cognito.userPoolId}")
    private String userPoolId;

	
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, 
		      Authentication authentication)
	{
    	String authorization = request.getHeader("Authorization");
        String accessToken = authorization.substring("Bearer ".length()).trim();
        
        System.out.println("IMPL: "+cognitoClient.getClass().getName());
        
        String userName = cognitoClient
			        		.getUser(GetUserRequest
			        				.builder()
			        				.accessToken(accessToken)
			        				.build())
			        		.username();
        cognitoClient.adminUserGlobalSignOut(AdminUserGlobalSignOutRequest
        		.builder()
        		.username(userName)
        		.userPoolId(userPoolId).build());
        
        try {
			response.sendRedirect("/login");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
 
}
