package edu.nus.java_ca.controller;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.SessionManagement;
import edu.nus.java_ca.service.UserService;

@Controller
@SessionAttributes("uSession")
public class HomeController {
	@Autowired
	UserService uService;
	
	@Autowired
	SessionManagement sess;
	
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
	public String login(HttpSession session, SessionStatus status) {
		if(sess.isLoggedIn(session, status)) {
			return "redirect:/home";
		}
		return "login/login";
	}
	
	// Receive login information from html form and authenticate
	@PostMapping("/authenticate")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
		User result = uService.findByUserEmail(email);
		System.out.println(result);
		if(result == null) {
			return "login/failedLogin";
		}
		// compare the email and password
		if(result.getEmail().equals(email.toLowerCase()) && result.getPassword().equals(password)) {
			sess.createSession(session, result);
			// set lastlogin date
			result.setLastLoginDate(new Date());
			uService.saveUser(result);
			return "redirect:/home";
		}
		return "login/failedLogin";
	}
	
//	private boolean isLoggedIn(HttpSession session) {
//		
//	}
	
	// page after login
	@RequestMapping("/home")
	public String index(HttpSession session, SessionStatus status) {
		if(!sess.isLoggedIn(session, status)) {
			return "redirect:/";
		}
		return "index";
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session, SessionStatus status) {
		if(session.getAttribute("uSession") == null) {
			return "redirect:/";
		}
		sess.removeSession(session, status);
		return "redirect:/";
	}
}