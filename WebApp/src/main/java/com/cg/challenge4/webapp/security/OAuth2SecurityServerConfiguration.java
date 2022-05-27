package com.cg.challenge4.webapp.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.nimbusds.jose.shaded.json.JSONArray;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class OAuth2SecurityServerConfiguration extends ResourceServerConfigurerAdapter {

	private final ResourceServerProperties resource;
	
	public static final String SIGNUP_URL = "/api/users/sign-up";
    public static final String SIGNIN_URL = "/api/users/sign-in";
	public static final String LOGIN_URL = "/login";
	public static final String HEALTH_URL = "/actuator/health";
	@Autowired
	private CustomLogoutHandler logoutHandler;

    

	public OAuth2SecurityServerConfiguration(ResourceServerProperties resource) {
		this.resource = resource;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
        List<String> permitAllEndpointList = Arrays.asList(SIGNUP_URL, SIGNIN_URL,LOGIN_URL,HEALTH_URL);

        http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests(expressionInterceptUrlRegistry -> expressionInterceptUrlRegistry
                        .antMatchers(permitAllEndpointList
                                .toArray(new String[permitAllEndpointList.size()]))
                        .permitAll().anyRequest().authenticated())
                .logout()
                .logoutUrl("/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
                .and()
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(new CognitoAccessTokenConverter());
        
        
        
		
//		oauth2->{
//			oauth2.jwt{
//				jwt->jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor());
//			}
//		}
//		http.cors();
//
//		http.csrf().disable();
//
//		//http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests().antMatchers("/oauth/token").permitAll().
//
//		
//		
//		http.
//		sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//		.and()
//		.authorizeRequests(authorize -> 
//		authorize.mvcMatchers("/**")
//			.permitAll()
//			.antMatchers("/actuator/health")
//			.permitAll()
//			.antMatchers("/oauth2/token")
//			.permitAll()
//			.anyRequest()
//			.authenticated()
//		.oauth2ResourceServer(
//				
//				oauth2 -> 
//					
//					oauth2.jwt(
//							jwt-> jwt.decoder(JwtDecoders.fromIssuerLocation(issuerUri))
//						)
//				
//		)	
//		.oauth2Login(
////					l->{
////        	 return l.userInfoEndpoint().userAuthoritiesMapper(userAuthoritiesMapper());
////		}
//		)
//		.and()
//		.logout()
//		.logoutSuccessUrl("/login"));
		
//	    http.authorizeRequests()
//        .antMatchers("/").permitAll()-
//        .anyRequest().authenticated();

//        .antMatchers("/")
//        .permitAll()
//        .antMatchers("/actuator/health")
//        .permitAll()
//        .anyRequest()
//        .authenticated()
	}
//
//	@Bean
//	public TokenStore jwkTokenStore() {
//		return new JwkTokenStore(Collections.singletonList(resource.getJwk().getKeySetUri()),
//				new CognitoAccessTokenConverter(), null);
//	}
//	

//    
//    public JwtAuthenticationConverter grantedAuthoritiesExtractor()
//    {
//    	JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//
//                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt ->{
//                    List<String> list = jwt.claims.getOrDefault("cognito:groups", new ArrayList<String>()); 
//
//                    list
//                    .map (obj -> obj.toString())
//                    .map (role -> SimpleGrantedAuthority(role));
//                }
//                );
//     
//
//                return jwtAuthenticationConverter;
//            
//    }

}