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

import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.service.LeaveBalanceService;
import edu.nus.java_ca.service.UserService;

@Controller
@RequestMapping("/AdminLeave")
public class AdminLeaveController {
	@Autowired
	UserService uService;
	
	@Autowired
	LeaveBalanceService lbService;
	
	@RequestMapping({ "/", "" })
	public String dashboard(Model model) {
		List<LeaveBalance> balancelist = lbService.findByLeavetype("annual");
		model.addAttribute("lblist", balancelist);
		return "admin/leavebalances";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editLeave(@PathVariable Integer id) {
		ModelAndView mav = new ModelAndView("admin/leave-form", "lbuser", lbService.findByBalId(id));
		return mav;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String editLeave(@ModelAttribute @Valid LeaveBalance lb, BindingResult result) {
		if (result.hasErrors()) {
			return "admin/leave-form";
		}
		lbService.saveLeaveBalance(lb);
		return "forward:/AdminLeave/";
	}
	

}
