package edu.nus.java_ca.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.UserRepository;


import edu.nus.java_ca.service.UserServiceImpl;
import edu.nus.java_ca.service.UserService;

@Controller
@RequestMapping("/AdminUser")
public class AdminUserController {

	@Autowired
	UserRepository urepo;
	
	@Autowired
	UserService Uservice;
	
	@Autowired
	public void setUserService(UserServiceImpl UserviceImpl) {
		this.Uservice = UserviceImpl;
	}
	
	@RequestMapping({"/", ""})
	public String dashboard(Model model) {
		List<User> userlist = Uservice.findAll();
		model.addAttribute("userlist", userlist);
		return "admin/users"; //folder is admin, users is the html
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addUser() {
		ModelAndView mav = new ModelAndView("admin/user-form", "user", new User());
		List<User> managerList = Uservice.findByPosition(Position.Manager);
		mav.addObject("managerlist", managerList);
		return mav;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveMember(@ModelAttribute("user") @Valid User user,
			BindingResult bindingResult,  Model model) {
		if (bindingResult.hasErrors()) {
			return "admin/user-form";
		}
		Uservice.saveUser(user);
		return "forward:/AdminUser/";
	}
	
	@RequestMapping(value = "/delete/{id}")
	public String deleteMember(@PathVariable("id") Long id) {
		
		Uservice.deleteUser(Uservice.findByUserId(id));
		return "forward:/AdminUser/";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView("admin/user-form", "user", Uservice.findByUserId(id));
		List<User> managerList = Uservice.findByPosition(Position.Manager);
		mav.addObject("managerlist", managerList);
		return mav;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String editUser(@ModelAttribute @Valid User user, BindingResult result, 
			@PathVariable Long id) {
		if (result.hasErrors()) {
			return "admin/user-form";
		}
		Uservice.saveUser(user);
		return "forward:/AdminUser/";
	}
	
}