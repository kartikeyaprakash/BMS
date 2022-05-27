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


import com.cg.challenge4.webapp.model.ShowTime;

@RestController
public class ShowTimeController {

	@Value("${theatre-service.url}")
	private String theatreServiceUrl;

	@Autowired
	private RestTemplate restTemplate;

	@PostMapping("/showTimes")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ShowTime> addShowTime(@RequestBody ShowTime showTime) {
		ShowTime newShowTime = restTemplate.postForObject(theatreServiceUrl+"/showTimes", showTime, ShowTime.class);
		return new ResponseEntity<>(newShowTime, HttpStatus.CREATED);
	}

	@GetMapping("/screens/{screenId}/showTimes")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<List<ShowTime>> getShowTimesForScreen(@PathVariable("screenId") String screenId) {
		
		ResponseEntity<List<ShowTime>> rateResponse =
		        restTemplate.exchange(theatreServiceUrl+"/screens/"+screenId+"/showTimes",
		                    HttpMethod.GET, null, new ParameterizedTypeReference<List<ShowTime>>() {
		            });
		List<ShowTime> showTime = rateResponse.getBody();
		return new ResponseEntity<>(showTime, HttpStatus.OK);
	}

	@GetMapping("/showTimes/{showTimeId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CUSTOMER')")
	public ResponseEntity<ShowTime> getShowTime(@PathVariable("showTimeId") String showTimeId) {
		ShowTime showtime = restTemplate.getForObject(theatreServiceUrl+"/showTimes"+"/"+showTimeId, ShowTime.class);
		return new ResponseEntity<>(showtime, HttpStatus.OK);
	}

	@PutMapping("/showTimes/{showTimeId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ShowTime> updateShowTime(@PathVariable("showTimeId") String showTimeId,
			@RequestBody ShowTime updatedShowTime) {
		HttpEntity<ShowTime> request = new HttpEntity<>(updatedShowTime);
		ResponseEntity<ShowTime> response = restTemplate
				  .exchange(theatreServiceUrl+"/showTimes"+"/"+showTimeId, HttpMethod.PUT, request, ShowTime.class);
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	}

	@DeleteMapping("/showTimes/{showTimeId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ShowTime> deleteShowTime(@PathVariable("showTimeId") String showTimeId) {
		HttpEntity<ShowTime> request = new HttpEntity<>(new ShowTime());
		ResponseEntity<ShowTime> response = restTemplate
				  .exchange(theatreServiceUrl+"/showTimes"+"/"+showTimeId, HttpMethod.DELETE, request, ShowTime.class);
		return new ResponseEntity<>(response.getBody(), HttpStatus.OK);
	}

}
