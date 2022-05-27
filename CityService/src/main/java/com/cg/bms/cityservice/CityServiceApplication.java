package com.cg.bms.cityservice;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.cityservice.exceptions.RestTemplateResponseErrorHandler;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;

@SpringBootApplication
public class CityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
		KeyStore keyStore;
		HttpComponentsClientHttpRequestFactory requestFactory = null;

		try {
			keyStore = KeyStore.getInstance("jks");
			ClassPathResource classPathResource = new ClassPathResource("city-service.jks");
			InputStream inputStream = classPathResource.getInputStream();
			keyStore.load(inputStream, "changeit".toCharArray());
			SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
					new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
							.loadKeyMaterial(keyStore, "changeit".toCharArray()).build(),
					NoopHostnameVerifier.INSTANCE);

			HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
					.setMaxConnTotal(Integer.valueOf(5)).setMaxConnPerRoute(Integer.valueOf(5)).build();

			requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
			requestFactory.setReadTimeout(Integer.valueOf(10000));
			requestFactory.setConnectTimeout(Integer.valueOf(10000));

			restTemplate.setRequestFactory(requestFactory);
		} catch (Exception exception) {
			System.out.println("Exception Occured while creating restTemplate " + exception);
			exception.printStackTrace();
		}

		return restTemplate;
	}

	@Bean
	public CircuitBreakerConfigCustomizer externalServiceCircuitBreakerConfig() {
		return CircuitBreakerConfigCustomizer.of("externalService",
				builder -> builder.slidingWindowSize(10).slidingWindowType(SlidingWindowType.COUNT_BASED)
						.waitDurationInOpenState(Duration.ofSeconds(5)).minimumNumberOfCalls(5)
						.failureRateThreshold(50.0f));
	}

}
