package com.cg.challenge4.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cg.challenge4.webapp.model.Screen;
import com.cg.challenge4.webapp.model.Seat;
import com.cg.challenge4.webapp.model.ShowTime;

@RestController
public class ScreenController {
	@Value("${theatre-service.url}")
	private String theatreServiceUrl;

	@Autowired
	private RestTemplate restTemplate;

	@PostMapping("/screens")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Screen> addScreen(@RequestBody Screen screen) {
		Screen newScreen = restTemplate.postForObject(theatreServiceUrl+"/screens", screen, Screen.class);
		return new ResponseEntity<>(newScreen, HttpStatus.CREATED);
	}

	@GetMapping("/theatres/{theatreId}/screens")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<List<Screen>> getScreensForTheatre(@PathVariable("theatreId") String theatreId) {
		ResponseEntity<List<Screen>> rateResponse =
		        restTemplate.exchange(theatreServiceUrl+"/theatres"+"/"+theatreId+"/screens",
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Screen>>() {
		            });
		List<Screen> screen = rateResponse.getBody();
		return new ResponseEntity<>(screen, HttpStatus.OK);
	}

	@PutMapping("/screens/{screenId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Screen> updateScreen(@PathVariable("screenId") String screenId,
			@RequestBody Screen updatedScreen) {
		//restTemplate.put(theatreServiceUrl+"/screens"+"/"+ screenId, updatedScreen);
		HttpEntity<Screen> request = new HttpEntity<>(updatedScreen);
		ResponseEntity<Screen> response = restTemplate
				  .exchange(theatreServiceUrl+"/screens"+"/"+ screenId, HttpMethod.PUT, request, Screen.class);
		
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	}

	@DeleteMapping("/screens/{screenId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> deleteScreen(@PathVariable("screenId") String screenId) {
		restTemplate.delete(theatreServiceUrl+"/screens"+"/"+ screenId);
		return ResponseEntity.ok("Screen with id: "+screenId+" deleted");
	}

}
