package com.cg.bms.theatreservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.cg.bms.theatreservice.model.City;
import com.cg.bms.theatreservice.model.Theatre;
import com.cg.bms.theatreservice.repository.TheatreRepository;


@ExtendWith(MockitoExtension.class)
public class TheatreServiceTest {
	
	

	@Mock
	private TheatreRepository theatreRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private TheatreService theatreService;
	
	
	@Value("${city-service.url}")
	private String cityServiceUrl;

	
	private Theatre theatre1;
	private Theatre theatre2;
	private List<Theatre> theatreList;
	private List<Theatre> theatreListByCity;
	private City cityforTheatre;

	
	@BeforeEach
	void setUp() throws Exception {
		theatreList = new ArrayList<>();
		theatre1 = Theatre.builder()._id("1").cityId("c1").name("t1").build();
		theatre2 = Theatre.builder()._id("2").cityId("c2").name("t2").build();
		theatreList.add(theatre1);
		theatreList.add(theatre2);
		theatreListByCity = new ArrayList<>();
		theatreListByCity.add(theatre2);
		cityforTheatre = City.builder()._id("c1").name("cname").state("s1").build();
		
	}
	
	@Test
	public void getCityOfTheatreForTheatreId()
	{
		when(theatreRepository.findById("1")).thenReturn(Optional.ofNullable(theatre1));
		when(restTemplate.getForObject(cityServiceUrl+"/cities/{cityId}", City.class, "c1")).thenReturn(cityforTheatre);
		City foundCity = theatreService.getCityOfTheatre(theatre1.get_id());
		assertThat(foundCity.getName()).isEqualTo(cityforTheatre.getName());
		
		
	}
	
	
	

	
	

}
