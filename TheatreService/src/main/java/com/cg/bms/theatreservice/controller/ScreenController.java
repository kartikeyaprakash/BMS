package com.cg.bms.theatreservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cg.bms.theatreservice.model.Screen;
import com.cg.bms.theatreservice.service.ScreenService;

@RestController
public class ScreenController {
	
	@Autowired
	private ScreenService screenService;
	
	@PostMapping("/screens")
	public ResponseEntity<Screen> addScreen(@RequestBody Screen screen)
	{
		return new ResponseEntity<>(screenService.addScreen(screen), HttpStatus.CREATED);
	}
	
	@GetMapping("/theatres/{theatreId}/screens")
	public  ResponseEntity<List<Screen>> getScreensForTheatre(@PathVariable("theatreId") String theatreId) 
	{
		return new ResponseEntity<>(screenService.getScreensForTheatre(theatreId), HttpStatus.OK);
	}
	
	@PutMapping("/screens/{screenId}")
	public ResponseEntity<Screen> updateScreen(@PathVariable("screenId") String screenId, @RequestBody Screen updatedScreen)
	{
		return new ResponseEntity<>(screenService.updateScreen(screenId, updatedScreen), HttpStatus.OK);
	}
	
	@DeleteMapping("/screens/{screenId}")
	public ResponseEntity<Screen> deleteScreen(@PathVariable("screenId") String screenId)
	{
		return new ResponseEntity<>(screenService.deleteScreen(screenId), HttpStatus.OK);
	}

}
