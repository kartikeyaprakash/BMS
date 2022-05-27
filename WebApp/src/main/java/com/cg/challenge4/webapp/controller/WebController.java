package com.cg.challenge4.webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cg.challenge4.webapp.model.Movie;
import com.cg.challenge4.webapp.model.MovieList;
import com.cg.challenge4.webapp.model.User;

@Controller
public class WebController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	//@Value(":")
	private final String userServiceUrl = "http://user-service/api/v1/user";
	
	@Value("${spring.application.name}") 
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }
    
    @GetMapping("/login")
    public String login() {
    	
    	return "login";
    }
    
    @GetMapping("/logout")
    public String logout() {
    	return "logout";
    }
    
    @GetMapping("/registration")
    public String registration(Model model) {
    	model.addAttribute("user",new User());
    	return "registration2";
    }
    
    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") User user,BindingResult bindingResult) {
    	//Rest template
    	User userform = restTemplate.postForObject(userServiceUrl, user, User.class);
    	if(userform == null)
    	return "registration";
    	else
    		return "home";
    	
    	//return "redirect:/welcome";
    }
    
   
    
    
}
