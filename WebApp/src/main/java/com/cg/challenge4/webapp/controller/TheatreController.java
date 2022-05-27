package com.cg.challenge4.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.cg.challenge4.webapp.model.City;
import com.cg.challenge4.webapp.model.Theatre;
import com.cg.challenge4.webapp.model.Theatres;

@RestController
public class TheatreController {
	@Value("${theatre-service.url}")
	private String theatreServiceUrl;

	@Autowired
	private RestTemplate restTemplate;

	@PostMapping("/theatres")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Theatre> addTheatre(@RequestBody Theatre theatre) {
		Theatre newTheatre = restTemplate.postForObject(theatreServiceUrl + "/theatres", theatre, Theatre.class);
		return new ResponseEntity<>(newTheatre, HttpStatus.CREATED);
	}

	@GetMapping("/theatres")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Theatres> getAllTheatres() {
		Theatres theatres = restTemplate.getForObject(theatreServiceUrl + "/theatres", Theatres.class);
		return new ResponseEntity<>(theatres, HttpStatus.OK);
	}

	@GetMapping("/theatres/{theatreId}")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Theatre> getTheatre(@PathVariable("theatreId") String theatreId) {
		Theatre theatre = restTemplate.getForObject(theatreServiceUrl+"/theatres"+"/"+ theatreId, Theatre.class);
		return new ResponseEntity<>(theatre, HttpStatus.OK);

	}

	@GetMapping("/cities/{cityId}/theatres")
	@PreAuthorize("hasAuthority('ROLE_CUSTOMER') or hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Theatres> getTheatresByCity(@PathVariable("cityId") String cityId) {
		Theatres theatres = restTemplate.getForObject(theatreServiceUrl +"/cities/"+cityId+"/theatres", Theatres.class);
		return new ResponseEntity<>(theatres, HttpStatus.OK);
	}

	@PutMapping("/theatres/{theatreId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Theatre> updateTheatre(@PathVariable("theatreId") String theatreId,
			@RequestBody Theatre updatedTheatre) {
//		restTemplate.put(theatreServiceUrl+"/"+ theatreId + "/theatres", updatedTheatre);
		HttpEntity<Theatre> request = new HttpEntity<>(updatedTheatre);
		ResponseEntity<Theatre> response = restTemplate
				  .exchange(theatreServiceUrl+ "/theatres"+"/"+ theatreId, HttpMethod.PUT, request, Theatre.class);
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	}

	@DeleteMapping("/theatres/{theatreId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Theatre> deleteTheatre(@PathVariable("theatreId") String theatreId) {
		Theatre theatre = restTemplate.getForObject(theatreServiceUrl+"/theatres"+"/"+ theatreId, Theatre.class);
		restTemplate.delete(theatreServiceUrl + "/theatres"+"/"+ theatreId);
		return new ResponseEntity<>(theatre, HttpStatus.OK);
	}

}
