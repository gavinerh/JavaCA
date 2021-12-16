package edu.nus.java_ca.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
	private UserService Uservice;
	
	@Autowired
	public void setUserService(UserServiceImpl UserviceImpl) {
		this.Uservice = UserviceImpl;
	}
	
	@RequestMapping(value = "/Ulist")
	public String list(Model model) {
		model.addAttribute("users", Uservice.findAll());
		return "users";
	}
	
	@RequestMapping(value = "/add")
	public String addForm(Model model) {
		model.addAttribute("user", new User());
		return "User-form";
	}
	@RequestMapping(value = "/save")
	public String saveMember(@ModelAttribute("user") @Valid User user, 
			BindingResult bindingResult,  Model model) {
		if (bindingResult.hasErrors()) {
			return "member-form";
		}
		Uservice.saveUser(user);
		return "forward:/AdminUser/Ulist";
	}
	@RequestMapping(value = "/delete/{id}")
	public String deleteMember(@PathVariable("id") Long id) {
		
		Uservice.deleteUser(Uservice.findByUserId(id));
		return "forward:/AdminUser/list";
	}
	
	
	
	
}