package edu.nus.java_ca.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.repository.UserRepository;
import edu.nus.java_ca.security.Hash;
import edu.nus.java_ca.service.LeaveBalanceService;
import edu.nus.java_ca.service.SessionManagement;
import edu.nus.java_ca.service.UserService;
import edu.nus.java_ca.service.UserServiceImpl;

@Controller
@RequestMapping("/AdminUser")
public class AdminUserController {

	@Autowired
	UserRepository urepo;

	@Autowired
	UserService Uservice;

	@Autowired
	LeaveBalanceService lbService;

	@Autowired
	SessionManagement sess;

	@Autowired
	public void setUserService(UserServiceImpl UserviceImpl) {
		this.Uservice = UserviceImpl;
	}

	@RequestMapping({ "/", "" })
	public String dashboard(Model model, HttpSession session, SessionStatus status) {

		String em = sess.getUserEmail(session);
		User result = Uservice.findByUserEmail(em);
		System.out.println(result.getLastName());
		model.addAttribute("usernow", result);

		List<User> userlist = Uservice.findAll();
		userlist.remove(result);

		model.addAttribute("userlist", userlist);
		return "admin/users"; // folder is admin, users is the html
	}

	// 1234
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addUser() {
		ModelAndView mav = new ModelAndView("admin/user-form", "user", new User());
		List<User> managerList = Uservice.findByPosition(Position.Manager);
		mav.addObject("managerlist", managerList);
		return mav;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveMember(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "admin/user-form";
		}
		Collection<LeaveBalance> lb = new ArrayList<LeaveBalance>();
		
		LeaveBalance lbAnnual = new LeaveBalance("annual", 0, user);
		LeaveBalance lbCompensation = new LeaveBalance("compensation", 0, user);
		LeaveBalance lbMedical = new LeaveBalance("medical", 0, user);
		
		lb.add(lbMedical);
		lb.add(lbCompensation);
		lb.add(lbAnnual);
		user.setLb(lb);

		Uservice.saveUser(user);
		return "forward:/AdminUser/";
	}
	
	@RequestMapping(value = "/delete/{id}")
	public String deleteMember(@PathVariable("id") Long id) {
		User delUser = Uservice.findByUserId(id);
		Uservice.deleteUser(delUser);

		Collection<LeaveBalance> userlbCollection = delUser.getLb(); 
		delUser.getLb().removeAll(userlbCollection);

		return "forward:/AdminUser/";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable Long id) {
		ModelAndView mav = new ModelAndView("admin/user-form-edit", "user", Uservice.findByUserId(id));
		List<User> managerList = Uservice.findByPosition(Position.Manager);
		mav.addObject("managerlist", managerList);
		return mav;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
		if (result.hasErrors()) {
			return "admin/user-form-edit";
		}
		Uservice.saveUser(user);
		return "forward:/AdminUser/";
	}

}