package edu.nus.java_ca.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.nus.java_ca.model.LeaveBalance;
import edu.nus.java_ca.model.Position;
import edu.nus.java_ca.model.User;
import edu.nus.java_ca.service.LeaveBalanceService;
import edu.nus.java_ca.service.UserService;

@Controller
@RequestMapping("/AdminType")
public class AdminLeaveTypeController {

	@Autowired
	UserService uService;
	
	@Autowired
	LeaveBalanceService lbService;


	@RequestMapping({ "/", "" })
	public String dashboard(Model model) {
		List<String> leavetypes = lbService.findAllLeaveTypes();
		model.addAttribute("ltlist", leavetypes);
		return "admin/leavetypes";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public ModelAndView addLeaveType() {
		ModelAndView mav = new ModelAndView("admin/leave-type-form", "newlt", new LeaveBalance());
		return mav;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String saveMember(@ModelAttribute("newlt") @Valid LeaveBalance lb,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "admin/leave-type-form";
		}

		ArrayList<User> userList = uService.findAllUserId();
		
		for (int i = 0; i < userList.size(); i++) {
			User x = userList.get(i);
			LeaveBalance newlb = new LeaveBalance(lb.getLeavetype(), lb.getBalance(), x);
			x.addLeaveBalance(newlb);
			lbService.saveLeaveBalance(newlb);
		}
		
//		for (User staffUser : userList) {
//			lb.setUser(staffUser);
//			staffUser.addLeaveBalance(lb);
//			lbService.saveLeaveBalance(lb);
//		}

		return "forward:/AdminType";
	}

	 

}
