package com.cg.bms.cityservice.apiTest;

import com.cg.bms.cityservice.model.City;
import com.cg.bms.cityservice.repository.CityRepository;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
properties = {"amazon.aws.accesskey=key", "amazon.aws.secretkey=key2"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("dev")
public class ExternalApiControllerTest {
    @RegisterExtension
    static WireMockExtension EXTERNAL_SERVICE = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().port(9090))
            .build();

    @Autowired
    private TestRestTemplate restTemplate;
       
    @MockBean
	private CityRepository cityRepository;
    
    @Value("${theatre-service.url}")
	private String theatreServiceUrl;
    
    private City city1;
	private City city2;
	private List<City> cityList;
    
    @BeforeEach
	void setUp() throws Exception {
		cityList = new ArrayList<>();
		List<String> theatre1 = new ArrayList<>();
		theatre1.add("theatreId1");
		List<String> theatre2 = new ArrayList<>();
		theatre2.add("theatreId1");
		
		city1 = new City("cityId1", "city1", "state1", theatre1);
		city2 = new City("cityId2", "city2", "state2", theatre2);
		cityList.add(city1);
		cityList.add(city2);
	}
    
    @Disabled
    @Test
    public void externalApiTest() throws Exception {
    	

    	when(cityRepository.findById(city1.get_id())).thenReturn(Optional.ofNullable(city1));
        EXTERNAL_SERVICE.stubFor(delete("theatreServiceUrl/101/theatres").willReturn(serverError()));

        for (int i = 0; i < 5; i++) {
        	
        	HttpHeaders headers = new HttpHeaders();
        	headers.setContentType(MediaType.APPLICATION_JSON);
        	
        	HttpEntity<City> requestUpdate = new HttpEntity<>(city1, headers);
            ResponseEntity resp = restTemplate.exchange("/cityId1/cities", HttpMethod.DELETE, requestUpdate, City.class);
            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            
        }
        for (int i = 0; i < 5; i++) {
        	
        	HttpHeaders headers = new HttpHeaders();
        	headers.setContentType(MediaType.APPLICATION_JSON);
        	
        	HttpEntity<City> requestUpdate = new HttpEntity<>(city1, headers);
            ResponseEntity resp = restTemplate.exchange("/cityId1/cities", HttpMethod.DELETE, requestUpdate, String.class);
            
            assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
            assertThat(resp.getBody()).asString().contains("CircuitBreaker 'externalService' is OPEN and does not permit further calls");
        }
    }
    
    @AfterEach
	void tearDown() throws Exception {
		city1 = null;
		city2 = null;
		cityList = null;
	}

}