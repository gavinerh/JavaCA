package edu.nus.java_ca.controller;

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
import edu.nus.java_ca.repository.LeaveBalanceRepo;
import edu.nus.java_ca.repository.UserRepository;
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
		if (!sess.isLoggedIn(session, status)) return "redirect:/";
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
	public ModelAndView addUser(HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return new ModelAndView("redirect:/");
		ModelAndView mav = new ModelAndView("admin/user-form", "user", new User());
		List<User> managerList = Uservice.findByPosition(Position.Manager);
		mav.addObject("managerlist", managerList);
		return mav;
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveMember(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		if (bindingResult.hasErrors()) {
			return "admin/user-form";
		}
		LeaveBalance lbAnnual = new LeaveBalance("annual", 10, user);
		LeaveBalance lbCompensation = new LeaveBalance("compensation", 10, user);
		LeaveBalance lbMedical = new LeaveBalance("medical", 10, user);
		user.addLeaveBalance(lbAnnual);
		user.addLeaveBalance(lbCompensation);
		user.addLeaveBalance(lbMedical);

		Uservice.saveUser(user);
		return "forward:/AdminUser/";
	}
	
	@RequestMapping(value = "/delete/{id}")
	public String deleteMember(@PathVariable("id") Long id, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		User delUser = Uservice.findByUserId(id);
		Uservice.deleteUser(delUser);

		Collection<LeaveBalance> userlbCollection = delUser.getLb(); 
		delUser.getLb().removeAll(userlbCollection);

		return "forward:/AdminUser/";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editUser(@PathVariable Long id, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return new ModelAndView("redirect:/");
		ModelAndView mav = new ModelAndView("admin/user-form", "user", Uservice.findByUserId(id));
		List<User> managerList = Uservice.findByPosition(Position.Manager);
		mav.addObject("managerlist", managerList);
		return mav;
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public String editUser(@ModelAttribute("user") @Valid User user, BindingResult result, @PathVariable Long id, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		if (result.hasErrors()) {
			return "admin/user-form";
		}
		Uservice.saveUser(user);
		return "forward:/AdminUser/";
	}

}