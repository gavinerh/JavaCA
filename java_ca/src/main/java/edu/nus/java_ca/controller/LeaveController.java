package edu.nus.java_ca.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.iss.laps.model.Leave;
import sg.iss.laps.service.LeaveBalanceService;
import sg.iss.laps.service.LeaveService;
import sg.iss.laps.service.LeaveServiceImpl;

@Controller
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	private LeaveService lservice;
	@Autowired
	public void setLeaveService(LeaveServiceImpl lserviceImpl) {
		this.lservice = lserviceImpl;
	}
	
//	@Autowired
//	private LeaveBalanceService lbservice;

	
//	Add
//	Cancel
	
	//view leave history of respective employee
	@RequestMapping(value="/empl-leavehistory")
	public String empLeaveHistSearchPage(Model model) {
		Leave l = new Leave();
		model.addAttribute("leave", l);
		return "empl-leavehistory";
	}
	
	@RequestMapping(value="/empl-search")
	public String searchResult(@ModelAttribute("id") @Valid Leave l, 
			BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "empl-leavehistory";
		}
		model.addAttribute("emleaves", lservice.findLeaveByUserId(l.getUser().getUserId()));
		return "forward:/leave/empl-leavehistory";
	}
	
	//manager actions
	@RequestMapping(value = "/list")
	public String list(Model model) {
		model.addAttribute("leaves", lservice.listLeaveToApprove());
		return "leave-toapprove";
	}
	
	@RequestMapping(value = "/approve/{id}")
	public String approveLeave(@PathVariable("id") Integer id) {
		lservice.approveLeave(lservice.findLeaveById(id));
		//leave balance has to be reduced for corresponding user
		return "forward:/leave/list";
	}
	
	//rejection require comment
	@RequestMapping(value = "/reject/{id}")
	public String rejectLeave(@PathVariable("id") Integer id) {
		lservice.rejectLeave(lservice.findLeaveById(id));
		return "forward:/leave/list";
	}

	
}
