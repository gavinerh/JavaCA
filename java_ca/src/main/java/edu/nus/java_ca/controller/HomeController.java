package edu.nus.java_ca.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.UserService;

@Controller
public class HomeController {
	@Autowired
	UserService uService;
	
	// display pre-login page
	@RequestMapping({"/", ""})
	public String preLogin(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "unloggedIn";
	}
	
	// display register new user form
	@RequestMapping("/register")
	public String register(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "register/register";
	}
	
	@PostMapping("/register")
	public String registerNewUser(@ModelAttribute("user") @Valid User user, BindingResult binding) {
		User result = uService.findByUserEmail(user.getEmail());
		if(result == null) {
			// create a new user
			uService.saveUser(user);
			return "register/registerSuccess";
		}
		// add validation
		binding.addError(null);
		return "register/register";
	}
	

	// display login form for user to login
	@RequestMapping("/login")
	public String login() {
		return "login/login";
	}
	
	// Receive login information from html form and authenticate
	@PostMapping("/authenticate")
	public String login(@RequestParam String email, @RequestParam String password) {
		User result = uService.findByUserEmail(email);
		System.out.println(result);
		if(result == null) {
			return "login/failedLogin";
		}
		// compare the email and password
		if(result.getEmail().equals(email.toLowerCase()) && result.getPassword().equals(password)) {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
			Instant now = LocalDateTime.now().atZone(ZoneId.of("Asia/Singapore")).toInstant();
			result.setLastLoginDate(Date.from(now));
			uService.saveUser(result);
			return "redirect:/home";
		}
		return "login/failedLogin";
		
	}
	
	// page after login
	@RequestMapping("/home")
	public String index() {
		return "index";
	}
}
