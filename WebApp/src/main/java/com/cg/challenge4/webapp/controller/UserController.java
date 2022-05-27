package com.cg.challenge4.webapp.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cg.challenge4.webapp.exception.CustomException;
import com.cg.challenge4.webapp.model.UserDetail;
import com.cg.challenge4.webapp.model.UserSignInRequest;
import com.cg.challenge4.webapp.model.UserSignInResponse;
import com.cg.challenge4.webapp.model.UserSignUpRequest;
import com.nimbusds.jose.shaded.json.JSONArray;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminAddUserToGroupRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminInitiateAuthResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminRespondToAuthChallengeRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminRespondToAuthChallengeResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminSetUserPasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthFlowType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AuthenticationResultType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChallengeNameType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.DeliveryMediumType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.GetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.MessageActionType;
import software.amazon.awssdk.services.sns.model.InvalidParameterException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;  

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
	
	private static final Logger LOG  = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CognitoIdentityProviderClient cognitoClient;

    @Value(value = "${aws.cognito.userPoolId}")
    private String userPoolId;
    @Value(value = "${aws.cognito.clientId}")
    private String clientId;
    
    @Value("${spring.security.oauth2.client.registration.cognito.clientId}")
    private String securityClientId;
    
    @Value(value = "${security.oauth2.resource.userInfoUri}")
    private String userInfoUri;

    @PostMapping(path = "/sign-up")
    public void signUp(@RequestBody  UserSignUpRequest userSignUpRequest) {
    	
    	LOG.info("Userpool ID: ", userPoolId.substring(10));
    	LOG.info("Client ID: ", clientId.substring(0, 10));
    	LOG.info("User info uri: ", userInfoUri.substring(0,25));
    	LOG.info("Sec client Id: ", securityClientId.substring(0,10));

        try {
        	
        	Map<String, String> clientMetadata = new HashMap<>();
        	clientMetadata.put("Role" , "ROLE_ADMIN");

            AttributeType emailAttr =
                    AttributeType.builder().name("email").value(userSignUpRequest.getEmail()).build();
            AttributeType emailVerifiedAttr =
                     AttributeType.builder().name("email_verified").value("true").build();

            AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder()
            		.userPoolId(userPoolId).username(userSignUpRequest.getUsername())
                    .temporaryPassword(userSignUpRequest.getPassword())
                    .userAttributes(emailAttr, emailVerifiedAttr)
                    .messageAction(MessageActionType.SUPPRESS)
                    .desiredDeliveryMediums(DeliveryMediumType.EMAIL)
                    .clientMetadata(clientMetadata)
                    .build();

            AdminCreateUserResponse createUserResult = cognitoClient.adminCreateUser(userRequest);
            cognitoClient.adminAddUserToGroup(AdminAddUserToGroupRequest.builder()
            		.groupName(userSignUpRequest.getGroup())
            		.userPoolId(userPoolId)
            		.username(userSignUpRequest.getUsername()).build());
            LOG.info("User " + createUserResult.user().username()
                    + " is created. Status: " + createUserResult.user().userStatus());

            // Disable force change password during first login
            AdminSetUserPasswordRequest adminSetUserPasswordRequest =
                     AdminSetUserPasswordRequest.builder()
                     .username(userSignUpRequest.getUsername())
                     .userPoolId(userPoolId)
                     .password(userSignUpRequest.getPassword())
                     .permanent(true).build();

            cognitoClient.adminSetUserPassword(adminSetUserPasswordRequest);

        } catch (CognitoIdentityProviderException e) {
           throw new CustomException(e.awsErrorDetails().errorMessage(), e.getCause());
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), e.getCause());
        }
    }



    @PostMapping(path = "/sign-in")
    public @ResponseBody  UserSignInResponse signIn(
            @RequestBody  UserSignInRequest userSignInRequest) {
    	
    	

        UserSignInResponse userSignInResponse = new UserSignInResponse();

        final Map<String, String> authParams = new HashMap<>();
        authParams.put("USERNAME", userSignInRequest.getUsername());
        authParams.put("PASSWORD", userSignInRequest.getPassword());

        final AdminInitiateAuthRequest authRequest = AdminInitiateAuthRequest.builder()
        		.authFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
        		.clientId(clientId)
                .userPoolId(userPoolId)
                .authParameters(authParams)
                .build();

        try { 
            AdminInitiateAuthResponse result = cognitoClient.adminInitiateAuth(authRequest);

            AuthenticationResultType authenticationResult = null;

            if (result.challengeName() != null && !result.challengeName().name().isEmpty()) {

            	LOG.info("Challenge Name is " + result.challengeName().name());

                if (result.challengeName().equals(ChallengeNameType.NEW_PASSWORD_REQUIRED)) {
                    if (userSignInRequest.getPassword() == null) {
                        throw new CustomException(
                                "User must change password " + result.challengeName().name());

                    } else {

                        final Map<String, String> challengeResponses = new HashMap<>();
                        challengeResponses.put("USERNAME", userSignInRequest.getUsername());
                        challengeResponses.put("PASSWORD", userSignInRequest.getPassword());
                        // add new password
                        challengeResponses.put("NEW_PASSWORD", userSignInRequest.getNewPassword());

                        final AdminRespondToAuthChallengeRequest request =
                                 AdminRespondToAuthChallengeRequest
                                 		.builder()
                                        .challengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                                        .challengeResponses(challengeResponses)
                                        .clientId(clientId)
                                        .userPoolId(userPoolId)
                                        .session(result.session())
                                        .build();

                        AdminRespondToAuthChallengeResponse resultChallenge =
                                cognitoClient.adminRespondToAuthChallenge(request);
                        authenticationResult = resultChallenge.authenticationResult();

                        userSignInResponse.setAccessToken(authenticationResult.accessToken());
                        userSignInResponse.setIdToken(authenticationResult.idToken());
                        userSignInResponse.setRefreshToken(authenticationResult.refreshToken());
                        userSignInResponse.setExpiresIn(authenticationResult.expiresIn());
                        userSignInResponse.setTokenType(authenticationResult.tokenType());
                    }

                } else {
                    throw new CustomException(
                            "User has another challenge " + result.challengeName().name());
                }
            } else {

            	LOG.info("User has no challenge");
                authenticationResult = result.authenticationResult();

                userSignInResponse.setAccessToken(authenticationResult.accessToken());
                userSignInResponse.setIdToken(authenticationResult.idToken());
                userSignInResponse.setRefreshToken(authenticationResult.refreshToken());
                userSignInResponse.setExpiresIn(authenticationResult.expiresIn());
                userSignInResponse.setTokenType(authenticationResult.tokenType());
            }

        } catch (InvalidParameterException e) {
            throw new CustomException(e.awsErrorDetails().errorMessage());
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
        //cognitoClient.shutdown();
        return userSignInResponse;

    }
    
    
//
//    @GetMapping(path = "/detail")
//    public @ResponseBody  UserDetail getUserDetail(HttpServletRequest request) {
//    	
//    	GetUserRequest getUserRequest = new GetUserRequest();
//    	String authorization = request.getHeader("Authorization");
//        String accessToken = authorization.substring("Bearer ".length()).trim();
//        getUserRequest.setAccessToken(accessToken);
//        GetUserResult getUserResult = cognitoClient.getUser(getUserRequest);
//        List<AttributeType> userAttributes = getUserResult.getUserAttributes();
//        for(AttributeType userAttribute: userAttributes)
//        {
//        	System.out.println(userAttribute.getName()+ ":"+userAttribute.getValue());
//        }
//        
//        GetGroupRequest groupRequest = new GetGroupRequest();
//        groupRequest.setUserPoolId(userPoolId);
//        groupRequest.setGroupName("ROLE_ADMIN");
//        GetGroupResult groupResult = cognitoClient.getGroup(groupRequest);
//        System.out.println("User Group: "+groupResult.getGroup().getGroupName());
//        String userName = getUserResult.getUsername();
//    	System.out.println("Username : "+userName);
//        UserDetail userDetail = new UserDetail();
//        userDetail.setFirstName("Test");
//        userDetail.setLastName("Buddy");
//        userDetail.setEmail("testbuddy@tutotialsbuddy.com");
//        GrantedAuthoritiesMapper map = userAuthoritiesMapper();
//        //map.
//        return userDetail;
//    }
//    
//    
    @GetMapping("/profile")
    public @ResponseBody String getUserProfile(HttpServletRequest request)
    {
//    	String authorization = request.getHeader("Authorization");
//      String accessToken = authorization.substring("Bearer ".length()).trim();
//
//    	GetUserRequest req =  GetUserRequest.builder().accessToken(accessToken).build();
//    	GetUserResponse res = cognitoClient.getUser(req);
//    	System.out.println("Token"+res.userAttributes());
    	return "";
    }
//    
//    @PostMapping("/refresh")
////    public @ResponseBody UserSignInResponse refreshToken(HttpServletRequest request)
////    {
////    	
////    }
    
//    @Bean
//    public static GrantedAuthoritiesMapper userAuthoritiesMapper() {
//        return (authorities) -> {
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//
//            Optional<OidcUserAuthority> awsAuthority = (Optional<OidcUserAuthority>) authorities.stream()
//                    .filter(grantedAuthority -> "ROLE_USER".equals(grantedAuthority.getAuthority()))
//                    .findFirst();
//
//            if (awsAuthority.isPresent()) {
//                mappedAuthorities = ((JSONArray) awsAuthority.get().getAttributes().get("cognito:groups")).stream()
//                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                        .collect(Collectors.toSet());
//            }
//
//            for(GrantedAuthority aut: mappedAuthorities)
//            {
//            	System.out.println(aut.getAuthority());
//            }
//            return mappedAuthorities;
//        };
//    }
    


}