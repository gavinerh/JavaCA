package edu.nus.java_ca.controller;

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
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.LeaveBalanceService;
import edu.nus.java_ca.service.SessionManagement;
import edu.nus.java_ca.service.UserService;

@Controller
@RequestMapping("/AdminType")
public class AdminLeaveTypeController {

	@Autowired
	UserService uService;
	
	@Autowired
	LeaveBalanceService lbService;
	
	@Autowired
	SessionManagement sess;


	@RequestMapping({ "/", "" })
	public String dashboard(Model model, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		List<String> leavetypes = lbService.findAllLeaveTypes();
		model.addAttribute("ltlist", leavetypes);
		return "admin/leavetypes";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addLeaveType(HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return new ModelAndView("redirect:/");
		ModelAndView mav = new ModelAndView("admin/leave-type-form", "newlt", new LeaveBalance());
		return mav;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveMember(@ModelAttribute("newlt") @Valid LeaveBalance lb,
			BindingResult bindingResult, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		if (bindingResult.hasErrors()) {
			return "admin/leave-type-form";
		}
		List<User> userlist = uService.findAll();
		
		for (User staffUser : userlist) {
			LeaveBalance newlb = new LeaveBalance(lb.getLeavetype(), lb.getBalance(), staffUser);
			staffUser.addLeaveBalance(newlb);
			lbService.saveLeaveBalance(newlb);
		}

		return "forward:/AdminType";
	}
	
	@RequestMapping(value = "/delete/{eleave}")
	public String deleteLeaveType(@PathVariable("eleave") String eleave, HttpSession ses, SessionStatus status) {
		if (!sess.isLoggedIn(ses, status)) return "redirect:/";
		List<User> userlist = uService.findAllWithDeleted();
		for (User staffUser : userlist) {
			lbService.deleteLeaveBalanceByType(eleave, staffUser);
		}
		
		return "forward:/AdminType";
	}

	 

}
