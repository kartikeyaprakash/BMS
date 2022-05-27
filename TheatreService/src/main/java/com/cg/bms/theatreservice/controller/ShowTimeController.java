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

import com.cg.bms.theatreservice.model.ShowTime;
import com.cg.bms.theatreservice.service.ShowTimeService;

@RestController
public class ShowTimeController {
	
	@Autowired
	private ShowTimeService showTimeService;
	
	@PostMapping("/showTimes")
	public ResponseEntity<ShowTime> addShowTime(@RequestBody ShowTime showTime)
	{
		return new ResponseEntity<>(showTimeService.addShowTime(showTime), HttpStatus.CREATED);
	}
	
	
	@GetMapping("screens/{screenId}/showTimes")
	public  ResponseEntity<List<ShowTime>> getShowTimesForScreen(@PathVariable("screenId") String screenId)
	{
		return new ResponseEntity<>(showTimeService.getShowTimesForScreen(screenId), HttpStatus.OK);
	}
	
	@GetMapping("/showTimes/{showTimeId}")
	public  ResponseEntity<ShowTime> getShowTime(@PathVariable("showTimeId") String showTimeId)
	{
		return new ResponseEntity<>(showTimeService.getShowTime(showTimeId), HttpStatus.OK);
	}
	
	
	@PutMapping("/showTimes/{showTimeId}")
	public ResponseEntity<ShowTime> updateShowTime(@PathVariable("showTimeId") String showTimeId, @RequestBody ShowTime updatedShowTime)
	{
		return new ResponseEntity<>(showTimeService.updateShowTime(showTimeId, updatedShowTime), HttpStatus.OK);
	}
	
	@DeleteMapping("/showTimes/{showTimeId}")
	public ResponseEntity<ShowTime> deleteShowTime(@PathVariable("showTimeId") String showTimeId)
	{
		return new ResponseEntity<>(showTimeService.deleteShowTime(showTimeId), HttpStatus.OK);
	}

}
