package edu.nus.java_ca.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.security.Hash;
import edu.nus.java_ca.service.SeedingImpl;
import edu.nus.java_ca.service.SessionManagement;
import edu.nus.java_ca.service.UserService;

@Controller
@SessionAttributes("uSession")
public class HomeController {
	@Autowired
	UserService uService;
	
	@Autowired
	SessionManagement sess;
	
	@Autowired
	SeedingImpl seed;
	
	// display register new user form
	@RequestMapping("/register")
	public String register(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "register/register";
	}
	
	@PostMapping("/register")
	public String registerNewUser(@ModelAttribute("user") @Valid User user, BindingResult binding,
			HttpSession session, SessionStatus status) {
		User result = uService.findByUserEmail(user.getEmail());
		if (result == null) { 
			// create a new user
			if(user.getEmail().equals("admin@admin")) {
				ObjectError err = new ObjectError("Username error", "Email cannot be used");
				binding.addError(err);
				return "register/register";
			}
			User u = (User) session.getAttribute("temp");
			if(u.getEmail().equals("admin@admin")) {
				// set the registered admin
				System.out.println("Code got here");
				session.invalidate();
			}
			seed.createSeedingUser();
			String password = user.getPassword();
			// hash the password
			String hashedPassword = Hash.hashPassword(password);
			user.setPassword(hashedPassword);
			uService.deleteUser(u);
			Collection<LeaveBalance> lb = new ArrayList<LeaveBalance>();
			LeaveBalance lbAnnual = new LeaveBalance("annual", 18, user);
			LeaveBalance lbCompensation = new LeaveBalance("compensation", 0, user);
			LeaveBalance lbMedical = new LeaveBalance("medical", 60, user);

			lb.add(lbMedical);
			lb.add(lbCompensation);
			lb.add(lbAnnual);
			user.setLb(lb);
			uService.saveUser(user);
			return "register/registerSuccess";
		}

		// add validation
		ObjectError err = new ObjectError("Username error", "Email is already used");
		binding.addError(err);
		return "register/register";
	}
	

	
	@RequestMapping({ "/login", "", "/" })
	public String login(HttpSession session, SessionStatus status) {
		if (sess.isLoggedIn(session, status)) {
			return "redirect:/home";
		}
		if (uService.findByUserEmail("admin@admin") == null && uService.findAll().size() == 0) {
			// create a default admin if not already created
			User admin = new User();
			admin.setEmail("admin@admin");
			admin.setPassword("password");
			admin.setPosition(Position.Admin);
			uService.saveUser(admin);
		}
		return "login/login";
	}
	
	
	@PostMapping("/authenticate")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, SessionStatus status) {
		User result = uService.findByUserEmail(email);
		if (result == null) {
			return "login/failedLogin";
		}
		if (result.getEmail().equals("admin@admin") && result.getPassword().equals("password") && !result.isDeleted()) {
			// admin logs in for the first time, create a temporary session
			session.setAttribute("temp", result);
			return "redirect:/adminInitialLogin";
		}
		// compare the email and password
		String storedPassword = result.getPassword();
		if (result.getEmail().equals(email.toLowerCase()) && storedPassword.equals(Hash.hashPassword(password))) {
			sess.createSession(session, result);
			// set lastlogin date
			result.setLastLoginDate(new Date());
			uService.saveUser(result);
			// check the position of the staff
			return getRedirectURL(result);
		}
		return "login/failedLogin";
	}
	

	
	@RequestMapping("/adminInitialLogin")
	public String adminInitialLogin(HttpSession session, SessionStatus status, Model model) {
		if (session.getAttribute("temp") == null) return "redirect:/";
		model.addAttribute("user", uService.findByUserEmail("admin@admin"));
		return "register/register";
	}
	
	
	// page after login
	@RequestMapping("/home")
	public String index(HttpSession session, SessionStatus status) {
		if(!sess.isLoggedIn(session, status)) {
			return "redirect:/";
		}
		User result = uService.findByUserEmail(sess.getUserEmail(session));
		return getRedirectURL(result);
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session, SessionStatus status) {
		if(session.getAttribute("uSession") == null) {
			return "redirect:/";
		}
		sess.removeSession(session, status);
		return "redirect:/";
	}
	
	private String getRedirectURL(User user) {
		switch(user.getPosition()) {
		case Admin:
			return "redirect:/AdminUser";
		case Manager:
			return "redirect:/manager/home";
		case Staff:
			return "redirect:/staff1/main";
		}
		return null;
	}
}
