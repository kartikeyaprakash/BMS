package com.cg.challenge4.webapp.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.shaded.json.JSONArray;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Component
public class CognitoAccessTokenConverter extends JwtAuthenticationConverter {
	private static final String COGNITO_GROUPS = "cognito:groups";

	public CognitoAccessTokenConverter() {
		setJwtGrantedAuthoritiesConverter(new CognitoGrantedAuthoritiesConverter());
	}

	private static class CognitoGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
		@Override
		public Collection<GrantedAuthority> convert(Jwt jwt) {
			// You can use the AWS Console to create Cognito groups and associate users.
			JSONArray groups = jwt.getClaim(COGNITO_GROUPS);
			Object groupsAsString = groups.toArray();
			// This converter requires the groups to be Spring roles, like ROLE_USER or
			// ROLE_ADMIN.
			Collection<GrantedAuthority> result = Arrays.stream(groups.toArray())
					.map(a -> new SimpleGrantedAuthority((String) a)).collect(Collectors.toList());
			return result;
		}
	}
}
